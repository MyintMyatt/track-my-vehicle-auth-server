package dev.orion.track_my_vehicle_auth_server.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record DeviceInfo(
        @NotEmpty(message = "{validation.empty} device ip")
        String ipAddress,
        @NotEmpty(message = "{validation.empty} device name")
        String deviceName,
        @NotEmpty(message = "{validation.empty} device id")
        String deviceId,
        String fcmToken
) {
}
