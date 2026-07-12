package dev.orion.track_my_vehicle_auth_server.utils;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserDetailsAdapter {
    String username();
    String password();
    Collection<? extends GrantedAuthority> authorities();
    boolean accountLocked();
    boolean accountExpired();
    boolean disabled();
}
