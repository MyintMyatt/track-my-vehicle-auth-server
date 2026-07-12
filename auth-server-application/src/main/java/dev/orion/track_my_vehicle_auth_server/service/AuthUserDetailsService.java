package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.entity.Account;
import dev.orion.auth.entity.AdminAccount;
import dev.orion.auth.entity.DriverAccount;
import dev.orion.auth.entity.EmployeeAccount;
import dev.orion.commons.exception.BusinessException;
import dev.orion.commons.exception.ExceptionMessageHolder;
import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.utils.AdminUserDetailsAdapter;
import dev.orion.track_my_vehicle_auth_server.utils.DriverUserDetailsAdapter;
import dev.orion.track_my_vehicle_auth_server.utils.EmployeeUserDetailsAdapter;
import dev.orion.track_my_vehicle_auth_server.utils.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public @Nullable UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var arr = username.split("-");
        var clientOrigin = ClientOrigin.valueOf(arr[0]);
        switch (clientOrigin) {
            case AdminPortal: {
                var email = arr[1];
                var admin = accountService.findAdminByEmail(email);
                return toUserDetails(admin, clientOrigin);
            }
            case EmployeeApp: {
                var email = arr[1];
                var employee = accountService.findEmployeeByEmail(email);

                return toUserDetails(employee, clientOrigin);
            }
            case DriverApp: {
                var phone = arr[1];
                var driver = accountService.findDriverByPhone(phone);
                return toUserDetails(driver, clientOrigin);
            }
            default: {
                return null;
            }
        }
    }

    private UserDetails toUserDetails(Account account, ClientOrigin clientOrigin){
        if (account == null){
            throw  new BusinessException(new ExceptionMessageHolder(
                    List.of(new ExceptionMessageHolder.Message("business.entity.notFound", new Object[]{"Account", "email or phone"})))
            );
        }
        var adapter = adapter(account, clientOrigin);
        if (adapter != null){
            return User.builder()
                    .username(adapter.username())
                    .password(adapter.password())
                    .authorities(adapter.authorities())
                    .accountLocked(adapter.accountLocked())
                    .accountExpired(adapter.accountExpired())
                    .disabled(adapter.disabled())
                    .build();
        }
        return null;
    }

    private UserDetailsAdapter adapter(Account account , ClientOrigin clientOrigin){
        switch (clientOrigin){
            case AdminPortal -> new AdminUserDetailsAdapter((AdminAccount) account);
            case EmployeeApp -> new EmployeeUserDetailsAdapter((EmployeeAccount) account);
            case DriverApp -> new DriverUserDetailsAdapter((DriverAccount) account);
        }
        return null;
    }
}
