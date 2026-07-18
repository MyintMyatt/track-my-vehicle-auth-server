package dev.orion.auth.entity;

import dev.orion.core.domain.account.constant.AccountStatus;
import dev.orion.core.domain.account.constant.DriverAccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "driver_account")
@Data
public class DriverAccount extends Account{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverAccountStatus driverAccountStatus;

    @Override
    public AccountStatus getAccountStatus() {
        return this.driverAccountStatus;
    }
}
