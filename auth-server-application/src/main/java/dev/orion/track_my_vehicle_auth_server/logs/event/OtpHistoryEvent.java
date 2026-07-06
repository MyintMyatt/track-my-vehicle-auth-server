package dev.orion.track_my_vehicle_auth_server.logs.event;

import dev.orion.auth.constant.OtpHistoryType;

public record OtpHistoryEvent(
        String email,
        OtpHistoryType type,
        boolean isUsed
        ) {
}
