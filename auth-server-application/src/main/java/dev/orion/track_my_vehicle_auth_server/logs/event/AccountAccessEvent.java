package dev.orion.track_my_vehicle_auth_server.logs.event;

import dev.orion.auth.constant.AccessStatus;
import dev.orion.auth.constant.AccessType;
import dev.orion.auth.embedded.AccessHistoryPk;
import dev.orion.auth.entity.AccountAccessHistory;
import dev.orion.track_my_vehicle_auth_server.dto.request.DeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountAccessEvent{
    String name; // unique name
    AccessType accessType;
    AccessStatus accessStatus;
    DeviceInfo deviceInfo;

    public static AccountAccessEvent loginSuccess(String name, DeviceInfo deviceInfo){
        return new AccountAccessEvent(name, AccessType.Login, AccessStatus.Success, deviceInfo);
    }

    public static AccountAccessEvent loginFail(String name,AccessStatus status, DeviceInfo deviceInfo){
        return new AccountAccessEvent(name, AccessType.Login, status, deviceInfo);
    }

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
