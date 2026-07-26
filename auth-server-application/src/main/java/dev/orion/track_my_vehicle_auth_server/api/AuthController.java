package dev.orion.track_my_vehicle_auth_server.api;

import dev.orion.commons.model.ApiResponse;
import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.dto.request.AuthRequest;
import dev.orion.track_my_vehicle_auth_server.dto.response.CheckEmployeeAccountResponse;
import dev.orion.track_my_vehicle_auth_server.dto.response.LoginResponse;
import dev.orion.track_my_vehicle_auth_server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestHeader("X-Client-Origin") ClientOrigin clientOrigin,
            @Valid @RequestBody AuthRequest request, BindingResult result) {
        System.err.println("call login");
        return ApiResponse.success(authService.login(clientOrigin, request));
    }

    @PostMapping("/check/account/{email}")
    public ApiResponse<CheckEmployeeAccountResponse> checkAccountByEmail(@PathVariable String email){
        return ApiResponse.success(authService.checkAccountByEmail(email));
    }
}
