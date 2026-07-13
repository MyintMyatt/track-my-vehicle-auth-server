package dev.orion.auth.constant;


import dev.orion.commons.utils.EnumResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;

public enum AccessStatus implements EnumResponse {
    Success("Success"),
    AccountLocked("Account Locked"),
    AccountDisabled("Account Disabled"),
    AccountExpired("Account Expired"),
    PasswordFailure("Wrong Credential"),
    Fail("Fail");

    private final String name;

    AccessStatus(String n){
        name = n;
    }

    public static AccessStatus from(AuthenticationException e){
        return switch (e) {
            case LockedException le -> AccountLocked;
            case DisabledException de -> AccountDisabled;
            case AccountExpiredException ee -> AccountExpired;
            case BadCredentialsException be -> PasswordFailure;
            case null, default -> Fail;
        };
    }


    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }
}
