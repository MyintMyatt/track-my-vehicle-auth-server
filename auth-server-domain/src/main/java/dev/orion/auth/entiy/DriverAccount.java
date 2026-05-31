package dev.orion.auth.entiy;

import dev.orion.account.constant.AccountStatus;
import dev.orion.account.constant.DriverAccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class DriverAccount extends Account{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverAccountStatus driverAccountStatus;

    @Override
    public AccountStatus getAccountStatus() {
        return this.driverAccountStatus;
    }
}
