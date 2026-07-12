package dev.orion.track_my_vehicle_auth_server.utils;

import dev.orion.account.constant.EmployeeAccountStatus;
import dev.orion.auth.entity.AdminAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AdminUserDetailsAdapter implements UserDetailsAdapter{

    private final AdminAccount account;

    public AdminUserDetailsAdapter(AdminAccount account){
        this.account = account;
    }

    @Override
    public String username() {
        return account.getName();
    }

    @Override
    public String password() {
        return account.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> authorities() {
        return List.of(new SimpleGrantedAuthority(account.getUserType().getName()), new SimpleGrantedAuthority(String.valueOf(account.getId())));
    }

    @Override
    public boolean accountLocked() {
        return account.getAccountStatus() == EmployeeAccountStatus.LOCKED;
    }

    @Override
    public boolean accountExpired() {
        return account.getAccountStatus() == EmployeeAccountStatus.CLOSED;
    }

    @Override
    public boolean disabled() {
        return account.getAccountStatus() == EmployeeAccountStatus.SUSPENDED ||
                account.getAccountStatus() == EmployeeAccountStatus.REJECT;
    }
}


