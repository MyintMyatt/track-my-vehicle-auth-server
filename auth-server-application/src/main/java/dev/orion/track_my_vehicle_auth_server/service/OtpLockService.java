package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.constant.LockSettingType;
import dev.orion.auth.constant.OtpHistoryType;
import dev.orion.auth.entity.OtpHistory;
import dev.orion.auth.repo.OtpHistoryRepo;
import dev.orion.exception.ExceptionMessageHolder;
import dev.orion.exception.OtpException;
import dev.orion.time.DateTimeUtils;
import dev.orion.time.TimeSetting;
import dev.orion.track_my_vehicle_auth_server.dto.input.OtpRequestForm;
import dev.orion.track_my_vehicle_auth_server.logs.event.OtpHistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpLockService {

    private final OtpHistoryRepo otpHistoryRepo;
    private final LockSettingService lockSettingService;
    private final ApplicationEventPublisher eventPublisher;

    public void checkUserIsInOtpTempLock(String email) {
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

        if (count > 0) {
            throw new OtpException(new ExceptionMessageHolder(
                    List.of(
                            new ExceptionMessageHolder.Message("security.otp.in.temp.lock.title"),
                            new ExceptionMessageHolder.Message("security.otp.in.temp.lock.desc", new Object[]{
                                    count, setting.getTemporaryLockOutValue(), setting.getTemporaryLockOutDurationUnit().toString(),
                                    DateTimeUtils.add(
                                            LocalDateTime.now(),
                                            new TimeSetting(setting.getTemporaryLockOutDurationUnit(), setting.getTemporaryLockOutValue())
                                    )
                            }),
                            new ExceptionMessageHolder.Message("security.otp.in.temp.lock.title.mm"),
                            new ExceptionMessageHolder.Message("security.otp.in.temp.lock.desc.mm", new Object[]{
                                    count, setting.getTemporaryLockOutValue(), setting.getTemporaryLockOutDurationUnit().toString(),
                                    DateTimeUtils.add(
                                            LocalDateTime.now(),
                                            new TimeSetting(setting.getTemporaryLockOutDurationUnit(), setting.getTemporaryLockOutValue())
                                    )
                            })
                    )
            ));
        }
    }

    public boolean checkUserReachOtpFailMaxAttempt(OtpRequestForm request) {
        var setting = lockSettingService.findSetting(LockSettingType.OtpLock);
        var attempts = otpHistoryRepo.findOne(cb -> {
            var cq = cb.createQuery(Long.class);
            var root = cq.from(OtpHistory.class);
            cq.select(cb.count(root));
            cq.where(
                    cb.and(
                            cb.equal(root.get("email"), request.email()),
                            cb.equal(root.get("otpHistoryType"), OtpHistoryType.FailedAttempt),
                            cb.greaterThanOrEqualTo(root.get("attemptWindowDurationUnit"), LocalDateTime.now().minus(setting.getTemporaryLockOutValue(), setting.getAttemptWindowDurationUnit()))
                    )
            );
            return cq;
        }).orElse(0L);

        if (attempts >= setting.getMaxFailedAttempts()) {
            eventPublisher.publishEvent(
                    new OtpHistoryEvent(
                            request.email(),
                            OtpHistoryType.TemporaryLockOut,
                            true
                    ));
            return true;
        } else {
            return false;
        }
    }

}
