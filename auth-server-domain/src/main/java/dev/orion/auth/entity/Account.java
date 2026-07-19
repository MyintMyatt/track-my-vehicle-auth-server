package dev.orion.auth.entity;

import dev.orion.core.domain.account.constant.AccountStatus;
import dev.orion.core.domain.account.constant.UserType;
import dev.orion.core.domain.auditor.AuditoryEntity;
import dev.orion.auth.embedded.UserName;
import dev.orion.core.domain.transaction.constant.TransactionState;
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

    @Embedded
    private UserName userName;

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

    private boolean permanentLock = false;

    @OneToOne(mappedBy = "account")
    private AccountAccess accountAccess;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private AccountRole accountRole;

    public abstract AccountStatus getAccountStatus();
}
