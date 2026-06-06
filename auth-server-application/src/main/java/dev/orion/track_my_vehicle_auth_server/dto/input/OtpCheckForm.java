package dev.orion.track_my_vehicle_auth_server.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OtpCheckForm(
        @NotBlank(message = "{validation.empty} email")
        String email,
        @NotBlank(message = "{validation.empty} otp code")
        @Size(min = 6, max = 6, message = "Wrong OTP")
        String otp
) {
}
