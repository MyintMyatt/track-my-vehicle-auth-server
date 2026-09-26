package dev.orion.track_my_vehicle_auth_server.service;

import com.ezsender.client.metadata.EzSenderRabbitMqMetadata;
import com.ezsender.client.models.NotificationRequest;
import com.ezsender.client.models.OtpRequest;
import dev.orion.auth.constant.LockSettingType;
import dev.orion.auth.constant.OtpHistoryType;
import dev.orion.auth.embedded.OtpHistoryPk;
import dev.orion.auth.entity.Account;
import dev.orion.commons.exception.auth.OtpException;
import dev.orion.core.domain.common.constant.SystemType;
import dev.orion.commons.utils.time.TimeSetting;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpRequestForm;
import dev.orion.track_my_vehicle_auth_server.logs.event.OtpHistoryEvent;
import dev.orion.track_my_vehicle_auth_server.utils.OtpCodeGeneratorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

//    private final EzSenderGrpcClient ezSenderGrpcClient;
    private final OtpLockService otpLockService;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public boolean send(SystemType clientType, OtpRequestForm form) {

       try{
//           // 1. At first check user is in otp temp lock
//           otpLockService.checkUserIsInOtpTempLock(form.username());
//           // 2. check user request OTP many times
//           otpLockService.checkUserIsInOtpLock(form, LockSettingType.OtpMaxRequestLock, OtpHistoryType.Requested);
//           // 3. check user enter wrong otp
//           otpLockService.checkUserIsInOtpLock(form, LockSettingType.OtpFailAttemptLock, OtpHistoryType.FailedAttempt);

           var otp = OtpCodeGeneratorUtils.generate();
           var eventData = new OtpRequest(null, "3 min", otp, "sign up verification");

           var notiEvent = new NotificationRequest(
                      "notification-id-123",
                   form.username(),
                   3,
                   OffsetDateTime.now(),
                   "8344611",
                   Locale.ENGLISH,
                   "Sign Up Verification",
                   objectMapper.convertValue(eventData, new TypeReference<Map<String, Object>>() {})

           );
           rabbitTemplate.convertAndSend(
                   EzSenderRabbitMqMetadata.MAIN_EXCHANGE,
                   "security.otp.email",
                    notiEvent,
                    message -> {
                       message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                       message.getMessageProperties().setPriority(5);
                       return message;
                    }
           );
           return true;
       }catch (Exception e){
           log.error("Error : {}", e.getMessage());
           throw e;
       }

    }

    @Override
    public boolean check(SystemType clientType, String otpCheckSum, OtpCheckForm form) {
        // TODO: implement otp key encryption / decryption
        var encryptedEmail = OtpHistoryPk.fromOtpKey(otpCheckSum).getUsername();
        if (!encryptedEmail.equals(form.username())){
            throw new OtpException("Invalid Otp");
        }

        if(!isValidateOtp(form.username(),form.otp())){
            eventPublisher.publishEvent(new OtpHistoryEvent(clientType + "-" + form.username(), OtpHistoryType.FailedAttempt, true));
            throw new OtpException("Invalid Otp");
        }
        eventPublisher.publishEvent(new OtpHistoryEvent(clientType + "-" + form.username(), OtpHistoryType.Verified, true));
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
