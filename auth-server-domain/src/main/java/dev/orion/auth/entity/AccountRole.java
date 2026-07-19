package dev.orion.auth.entity;

import dev.orion.core.domain.auditor.AuditoryEntity;
import dev.orion.core.domain.common.constant.SystemType;
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
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SystemType systemType;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<AccountPermission> permissions = new HashSet<>();
}
