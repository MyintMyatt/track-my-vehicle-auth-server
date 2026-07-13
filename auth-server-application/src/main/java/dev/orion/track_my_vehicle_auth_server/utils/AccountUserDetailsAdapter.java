package dev.orion.track_my_vehicle_auth_server.utils;

import dev.orion.auth.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AccountUserDetailsAdapter implements UserDetailsAdapter {
    private final Account account;

    public AccountUserDetailsAdapter(Account account) {
        this.account = account;
    }

    @Override
    public String username() {
        return account.getUserName().getUniqueName();
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
        return account.isPermanentLock();
    }

    @Override
    public boolean accountExpired() {
        return account.getAuditInfo().isDeleted();
    }

    @Override
    public boolean disabled() {
        return account.isPermanentLock();
    }
}
