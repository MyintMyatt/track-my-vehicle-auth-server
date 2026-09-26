package dev.orion.track_my_vehicle_auth_server.api;

import dev.orion.commons.model.ApiResponse;
import dev.orion.core.domain.common.constant.SystemType;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpRequestForm;
import dev.orion.track_my_vehicle_auth_server.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ApiResponse<Boolean> send(
        @RequestHeader(name = "X-Client-Type", required = true) SystemType clientType,
        @RequestBody OtpRequestForm form,
        BindingResult result
        ){
        return ApiResponse.success(otpService.send(clientType, form));
    }

    @PostMapping("/check")
    public ApiResponse<Boolean> check(
            @RequestHeader(name = "X-Client-Type", required = true) SystemType clientType, 
            @RequestHeader("X-OTP-CHECKSUM") String otpChecksum,
            @RequestBody OtpCheckForm form,
            BindingResult result){
        return ApiResponse.success(otpService.check(clientType, otpChecksum, form));
    }
}
