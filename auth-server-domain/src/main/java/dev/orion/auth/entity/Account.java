package dev.orion.auth.entity;

import dev.orion.account.constant.AccountStatus;
import dev.orion.account.constant.UserType;
import dev.orion.auditor.AuditoryEntity;
import dev.orion.transaction.constant.TransactionState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "account")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account extends AuditoryEntity {
    @Id
    @GeneratedValue(generator = "seq_generator")
    private long id;

    @Column(nullable = false)
    private String name;

    private String fullName;

    private String tenantId; // for multi clients support in the future

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionState transactionState;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private AccountAccess accountAccess;

    public abstract AccountStatus getAccountStatus();
}
