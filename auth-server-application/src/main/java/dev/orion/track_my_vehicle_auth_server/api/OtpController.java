package dev.orion.track_my_vehicle_auth_server.api;

import dev.orion.commons.model.ApiResponse;
import dev.orion.track_my_vehicle_auth_server.dto.input.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.input.OtpRequestForm;
import dev.orion.track_my_vehicle_auth_server.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/v1/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ApiResponse<Boolean> send(OtpRequestForm form, BindingResult result){
        return ApiResponse.success(otpService.send(form));
    }

    @PostMapping("/check")
    public ApiResponse<Boolean> check(
            @RequestHeader("X-OTP-CHECKSUM") String otpChecksum,
            OtpCheckForm form, BindingResult result){
        return ApiResponse.success(otpService.check(otpChecksum, form));
    }
}
