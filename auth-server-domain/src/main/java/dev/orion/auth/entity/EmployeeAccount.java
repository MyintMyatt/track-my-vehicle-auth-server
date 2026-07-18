package dev.orion.auth.entity;

import dev.orion.core.domain.account.constant.AccountStatus;
import dev.orion.core.domain.account.constant.EmployeeAccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employee_account")
@Data
public class EmployeeAccount extends Account{

    @Column(nullable = false)
    private String email;

    private EmployeeAccountStatus employeeAccountStatus;

    @Override
    public AccountStatus getAccountStatus() {
        return this.employeeAccountStatus;
    }
}
