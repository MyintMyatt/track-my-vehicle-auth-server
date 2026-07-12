package dev.orion.track_my_vehicle_auth_server.logs.event;

import dev.orion.auth.constant.AccessStatus;
import dev.orion.auth.constant.AccessType;
import dev.orion.auth.embedded.AccessHistoryPk;
import dev.orion.auth.entity.AccountAccessHistory;
import dev.orion.track_my_vehicle_auth_server.dto.input.DeviceInfo;

import java.time.LocalDateTime;

public record AccountAccessEvent(
    long accountId,
    String name,
    AccessType accessType,
    AccessStatus accessStatus,
    DeviceInfo deviceInfo
) {
    public AccountAccessHistory history(){
        var entity = new AccountAccessHistory();
        var id = new AccessHistoryPk();
        id.setUsername(name);
        id.setAccessAt(LocalDateTime.now());
        entity.setAccessHistoryPk(id);
        entity.setAccessType(accessType);
        entity.setAccessStatus(accessStatus);
        entity.setDevice(deviceInfo.deviceName());
        entity.setDeviceId(deviceInfo.deviceId());
        entity.setIpAddress(deviceInfo.ipAddress());
        return entity;
    }
}
