package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.constant.LockSettingType;
import dev.orion.auth.constant.OtpHistoryType;
import dev.orion.auth.embedded.OtpHistoryPk;
import dev.orion.commons.client.notification.grpc.NotificationClient;
import dev.orion.commons.exception.ExceptionMessageHolder;
import dev.orion.commons.exception.ServiceException;
import dev.orion.commons.exception.auth.OtpException;
import dev.orion.commons.utils.time.TimeSetting;
import dev.orion.grpc.notification.OtpNotificationRequest;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpRequestForm;
import dev.orion.track_my_vehicle_auth_server.logs.event.OtpHistoryEvent;
import dev.orion.track_my_vehicle_auth_server.utils.OtpCodeGeneratorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final NotificationClient notificationClient;
    private final OtpLockService otpLockService;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean send(OtpRequestForm form) {

        // 1. At first check user is in otp temp lock
        otpLockService.checkUserIsInOtpTempLock(form.email());
        // 2. check user request OTP many times
        otpLockService.checkUserIsInOtpLock(form, LockSettingType.OtpMaxRequestLock, OtpHistoryType.Requested);
        // 3. check user enter wrong otp
        otpLockService.checkUserIsInOtpLock(form, LockSettingType.OtpFailAttemptLock, OtpHistoryType.FailedAttempt);

        var otp = OtpCodeGeneratorUtils.generate();
        var otpRequest = OtpNotificationRequest.newBuilder().setEmail(form.email()).setOtp(otp).build();
        notificationClient.sendOtp(otpRequest).thenAccept(response -> {
            if (response.getSuccess()) {
                saveOtp(form.email(), otp, otpExpTime());
                eventPublisher.publishEvent(new OtpHistoryEvent(form.email(), OtpHistoryType.Requested, false));
            }
        }).exceptionally(err -> {
            throw new ServiceException(new ExceptionMessageHolder(new ExceptionMessageHolder.Message("service.unavailable", new Object[]{"Otp Request"})));
        });
        return true;
    }

    @Override
    public boolean check(String otpCheckSum, OtpCheckForm form) {
        // TODO: implement otp key encryption / decryption
        var encryptedEmail = OtpHistoryPk.fromOtpKey(otpCheckSum).getEmail();
        if (!encryptedEmail.equals(form.email())){
            throw new OtpException("Invalid Otp");
        }

        if(!isValidateOtp(form.email(),form.otp())){
            eventPublisher.publishEvent(new OtpHistoryEvent(form.email(), OtpHistoryType.FailedAttempt, true));
            throw new OtpException("Invalid Otp");
        }
        eventPublisher.publishEvent(new OtpHistoryEvent(form.email(), OtpHistoryType.Verified, true));
        return true;
    }

    private void saveOtp(String email, String otp, TimeSetting expirationTime){
        redisTemplate.opsForValue().set(email, otp, expirationTime.value(), TimeUnit.of(expirationTime.unit()));
    }

    private boolean isValidateOtp(String email, String otp){
        var result = redisTemplate.opsForValue().get(email);
        if(!otp.equals(result)){
            return false;
        }
        redisTemplate.delete(email);
        return true;
    }

    private TimeSetting otpExpTime(){
        return new TimeSetting(ChronoUnit.MINUTES, 3);
    }
}
