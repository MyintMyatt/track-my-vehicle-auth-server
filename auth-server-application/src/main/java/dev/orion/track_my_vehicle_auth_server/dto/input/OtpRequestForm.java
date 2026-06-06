package dev.orion.track_my_vehicle_auth_server.dto.input;

import jakarta.validation.constraints.NotBlank;

public record OtpRequestForm(
        @NotBlank(message = "{validation.empty} email")
        String email,
        boolean isResend
) {
}
