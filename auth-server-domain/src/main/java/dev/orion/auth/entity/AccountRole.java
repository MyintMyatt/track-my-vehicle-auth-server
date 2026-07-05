package dev.orion.auth.entity;

import dev.orion.auditor.AuditoryEntity;
import dev.orion.common.constant.SystemType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "account_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRole extends AuditoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "account_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns =  @JoinColumn(name = "permission_id")
    )
    private String name;

    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SystemType systemType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "account_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns =  @JoinColumn(name = "permission_id")
    )
    private Set<AccountPermission> permissions = new HashSet<>();
}
