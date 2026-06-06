package dev.orion.track_my_vehicle_auth_server.dto.input;

import jakarta.validation.constraints.NotEmpty;

public record DeviceInfo(
        @NotEmpty(message = "{validation.empty} device ip")
        String ipAddress,
        String deviceName,
        @NotEmpty(message = "{validation.empty} device id")
        String deviceId,
        @NotEmpty(message = "{validation.empty} device fcm")
        String fcmToken
) {
}
