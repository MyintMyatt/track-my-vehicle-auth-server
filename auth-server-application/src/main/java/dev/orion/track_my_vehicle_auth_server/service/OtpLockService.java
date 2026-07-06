package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.constant.LockSettingType;
import dev.orion.auth.constant.OtpHistoryType;
import dev.orion.auth.entity.OtpHistory;
import dev.orion.auth.repo.OtpHistoryRepo;
import dev.orion.track_my_vehicle_auth_server.logs.event.OtpHistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpLockService {

    private final OtpHistoryRepo otpHistoryRepo;
    private final LockSettingService lockSettingService;
    private final ApplicationEventPublisher eventPublisher;

    public boolean checkUserIsInOtpTempLock(String email) {
        var setting = lockSettingService.findSetting(LockSettingType.OtpLock);
        var count = otpHistoryRepo.findOne(cb -> {
            var cq = cb.createQuery(Long.class);
            var root = cq.from(OtpHistory.class);
            cq.select(cb.count(root));
            cq.where(
                    cb.and(
                            cb.equal(root.get("email"), email),
                            cb.equal(root.get("otpHistoryType"), OtpHistoryType.TemporaryLockOut),
                            cb.greaterThanOrEqualTo(root.get("attemptWindowDurationUnit"), LocalDateTime.now().minus(setting.getTemporaryLockOutValue(), setting.getAttemptWindowDurationUnit()))
                    )
            );
            return cq;
        }).orElse(0L);
        return count > 0;
    }

    public boolean checkUserReachOtpFailMaxAttempt(String email) {
        var setting = lockSettingService.findSetting(LockSettingType.OtpLock);
        var attempts = otpHistoryRepo.findOne(cb -> {
            var cq = cb.createQuery(Long.class);
            var root = cq.from(OtpHistory.class);
            cq.select(cb.count(root));
            cq.where(
                    cb.and(
                            cb.equal(root.get("email"), email),
                            cb.equal(root.get("otpHistoryType"), OtpHistoryType.FailedAttempt),
                            cb.greaterThanOrEqualTo(root.get("attemptWindowDurationUnit"), LocalDateTime.now().minus(setting.getTemporaryLockOutValue(), setting.getAttemptWindowDurationUnit()))
                    )
            );
            return cq;
        }).orElse(0L);

        if (attempts > setting.getMaxFailedAttempts()) {
            eventPublisher.publishEvent(
                    new OtpHistoryEvent(
                            email,
                            OtpHistoryType.TemporaryLockOut,
                            true
                    ));
            return true;
        } else {
            return false;
        }
    }

}
