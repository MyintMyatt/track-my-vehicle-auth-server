package dev.orion.track_my_vehicle_auth_server.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public record AuthRequest(
        @NotBlank(message = "{validation.empty} username")
        String username,
        @NotBlank(message = "{validation.empty} password")
        String password,
        @NotNull(message = "{validation.empty} device info") @Valid DeviceInfo deviceInfo
) {
        public Authentication authentication(String clientOrigin){
                return UsernamePasswordAuthenticationToken.unauthenticated(clientOrigin + "-" + username, password);
        }
}
