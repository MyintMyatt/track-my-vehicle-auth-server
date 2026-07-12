package dev.orion.track_my_vehicle_auth_server.api;

import dev.orion.commons.model.ApiResponse;
import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.dto.input.AuthRequest;
import dev.orion.track_my_vehicle_auth_server.dto.output.CheckEmployeeAccountResponse;
import dev.orion.track_my_vehicle_auth_server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    private ApiResponse<?> login(
            @RequestHeader("X-Client-Origin") ClientOrigin clientOrigin,
            AuthRequest request, BindingResult result) {
        return ApiResponse.success(authService.login(clientOrigin, request));
    }

    @PostMapping("/check/account/{email}")
    private ApiResponse<CheckEmployeeAccountResponse> checkAccountByEmail(@PathVariable String email){
        return ApiResponse.success(authService.checkAccountByEmail(email));
    }
}
