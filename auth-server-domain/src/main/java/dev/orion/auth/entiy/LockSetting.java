package dev.orion.auth.entiy;

import dev.orion.auditor.AuditoryEntity;
import dev.orion.auth.constant.LockSettingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.temporal.ChronoUnit;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lock_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockSetting extends AuditoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String tenantId;

    private Integer id;
    @Enumerated(EnumType.STRING)
    private LockSettingType lockSettingType;

    private int maxFailedAttempts;

    // The timeframe where failed attempts are counted
    private int attemptWindowValue;
    private ChronoUnit attemptWindowDurationUnit;

    private int temporaryLockOutValue;
    private ChronoUnit temporaryLockOutDurationUnit;
}
