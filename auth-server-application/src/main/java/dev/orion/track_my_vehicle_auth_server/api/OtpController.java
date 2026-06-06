package dev.orion.track_my_vehicle_auth_server.api;

import dev.orion.common.model.ApiResponse;
import dev.orion.track_my_vehicle_auth_server.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publice/v1/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ApiResponse<?> sendOtp(){
        return ApiResponse.success(null);
    }
}
