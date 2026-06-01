package dev.orion.auth.constant;

import dev.orion.common.utils.EnumResponse;

public enum AccessStatus implements EnumResponse {
    Success("Success"),
    AccountLocked("Account Locked"),
    AccountDisabled("Account Disabled"),
    AccountExpired("Account Expried"),
    Fail("Fail");

    private final String name;

    AccessStatus(String n){
        name = n;
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
