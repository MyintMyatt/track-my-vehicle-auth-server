package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.entity.Account;
import dev.orion.auth.entity.AdminAccount;
import dev.orion.auth.entity.DriverAccount;
import dev.orion.auth.entity.EmployeeAccount;
import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var arr = username.split("-");
        var clientOrigin = ClientOrigin.valueOf(arr[0]);
        switch (clientOrigin) {
            case AdminPortal: {
                var email = arr[1];
                return accountService.findAdminByEmailOrUserName(email, email)
                        .map(a -> toUserDetails(a, clientOrigin)).orElseThrow(() -> new UsernameNotFoundException(email));
            }
            case EmployeeApp: {
                var email = arr[1];
                return accountService.findEmployeeByEmailOrName(email, email)
                        .map(e -> toUserDetails(e, clientOrigin)).orElseThrow(() -> new UsernameNotFoundException(email));
            }
            case DriverApp: {
                var phone = arr[1];
                return accountService.findDriverByPhoneOrUserName(phone, phone)
                        .map(e -> toUserDetails(e, clientOrigin)).orElseThrow(() -> new UsernameNotFoundException(phone));
            }
            default: {
                var name = arr[1];
                var account = accountService.findAccountByUserName(name);
                return toUserDetails(account, null);
            }
        }
    }

    private UserDetails toUserDetails(Account account, ClientOrigin clientOrigin){

        var adapter = adapter(account, clientOrigin);
        return User.builder()
                .username(adapter.username())
                .password(adapter.password())
                .authorities(adapter.authorities())
                .accountLocked(adapter.accountLocked())
                .accountExpired(adapter.accountExpired())
                .disabled(adapter.disabled())
                .build();

    }

    private UserDetailsAdapter adapter(Account account , ClientOrigin clientOrigin){
        return switch (clientOrigin){
            case AdminPortal -> new AdminUserDetailsAdapter((AdminAccount) account);
            case EmployeeApp -> new EmployeeUserDetailsAdapter((EmployeeAccount) account);
            case DriverApp -> new DriverUserDetailsAdapter((DriverAccount) account);
            default -> new AccountUserDetailsAdapter(account);
        };
    }
}
