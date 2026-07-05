package dev.orion.auth.entity;

import dev.orion.account.constant.AccountStatus;
import dev.orion.account.constant.EmployeeAccountStatus;
import jakarta.persistence.Column;

public class EmployeeAccount extends Account{

    @Column(nullable = false)
    private String email;

    private EmployeeAccountStatus employeeAccountStatus;

    @Override
    public AccountStatus getAccountStatus() {
        return this.employeeAccountStatus;
    }
}
