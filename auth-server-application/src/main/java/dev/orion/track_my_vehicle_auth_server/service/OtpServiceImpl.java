package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.client.notification.grpc.NotificationClient;
import dev.orion.grpc.notification.OtpNotificationRequest;
import dev.orion.track_my_vehicle_auth_server.dto.input.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.input.OtpRequestForm;
import dev.orion.track_my_vehicle_auth_server.utils.OtpCodeGeneratorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final NotificationClient notificationClient;
    private final OtpLockService otpLockService;

    @Override
    public boolean send(OtpRequestForm form) {
        if (otpLockService.checkUserIsInOtpTempLock(form.email())) {
            var otp = OtpCodeGeneratorUtils.generate();
            var otpRequest = OtpNotificationRequest.newBuilder().setEmail(form.email()).setOtp(otp).build();
            notificationClient.sendOtp(otpRequest).thenAccept(response -> {
                if (response.getSuccess()) {

                }
            }).exceptionally(err -> {
                return null;
            });
        }
        return false;
    }

    @Override
    public String check(OtpCheckForm form) {
        return "";
    }
}
