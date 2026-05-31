package dev.orion.auth.entiy;


import dev.orion.account.constant.AccountStatus;
import dev.orion.account.constant.AdminAccountStatus;
import jakarta.persistence.*;

public class AdminAccount extends Account{

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminAccountStatus adminAccountStatus;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private AccountRole accountRole;

    @Override
    public AccountStatus getAccountStatus() {
        return this.adminAccountStatus;
    }
}
