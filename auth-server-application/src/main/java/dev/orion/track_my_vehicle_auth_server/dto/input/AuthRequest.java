package dev.orion.track_my_vehicle_auth_server.dto.input;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "{validation.empty} username")
        String username,
        @NotBlank(message = "{validation.empty} password")
        String password,
        DeviceInfo deviceInfo
) {
}
