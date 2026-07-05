package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.constant.LockSettingType;
import dev.orion.auth.entity.OtpHistory;
import dev.orion.auth.repo.OtpHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpHelperService {

    private final OtpHistoryRepo otpHistoryRepo;
    private final LockSettingService lockSettingService;

    public boolean checkOtpLock(String email) {
        // TODO: Validation for OTP request (e.g. OTP Lock, Max Attempt Fail)

        var setting = lockSettingService.findSetting(LockSettingType.OtpLock);
        var history = otpHistoryRepo.findOne(cb -> {
            var cq = cb.createQuery(OtpHistory.class);
            var root = cq.from(OtpHistory.class);
            cq.where(
                    cb.equal(root.get("email"), email)
            );
            return cq;
        });
        return false;
    }

}
