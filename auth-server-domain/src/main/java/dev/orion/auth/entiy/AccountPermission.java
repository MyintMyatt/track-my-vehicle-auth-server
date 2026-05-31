package dev.orion.auth.entiy;

import dev.orion.auditor.AuditoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "account_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPermission extends AuditoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String resource; // order, product, etc.

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<AccountRole> role;

    @Column(name = "permission_request", nullable = false)
    private boolean request = false;
    @Column(name = "permission_approve", nullable = false)
    private boolean approve = false;
    @Column(name = "permission_reject", nullable = false)
    private boolean reject= false;
    @Column(name = "permission_edit", nullable = false)
    private boolean edit= false;
    @Column(name = "permission_view", nullable = false)
    private boolean view= false;
    @Column(name = "permission_export", nullable = false)
    private boolean export= false; // for exporting report
    @Column(name = "permission_delete", nullable = false)
    private boolean delete= false;


}
