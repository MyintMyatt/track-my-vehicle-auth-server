package dev.orion.auth.entity;


import dev.orion.core.domain.account.constant.AccountStatus;
import dev.orion.core.domain.account.constant.AdminAccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admin_account")
@Data
public class AdminAccount extends Account{

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminAccountStatus adminAccountStatus;

    @Override
    public AccountStatus getAccountStatus() {
        return this.adminAccountStatus;
    }
}
