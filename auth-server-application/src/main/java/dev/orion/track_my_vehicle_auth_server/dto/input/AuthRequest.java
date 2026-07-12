package dev.orion.track_my_vehicle_auth_server.dto.input;

import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public record AuthRequest(
        @NotBlank(message = "{validation.empty} username")
        String username,
        @NotBlank(message = "{validation.empty} password")
        String password,
        DeviceInfo deviceInfo
) {
        public Authentication authentication(String clientOrigin){
                return UsernamePasswordAuthenticationToken.unauthenticated(clientOrigin + "-" + username, password);
        }
}
