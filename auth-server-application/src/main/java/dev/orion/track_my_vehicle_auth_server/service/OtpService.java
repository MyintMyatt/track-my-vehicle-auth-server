package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.core.domain.common.constant.SystemType;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpRequestForm;

public interface OtpService {

    boolean send(SystemType clientType, OtpRequestForm form);
    boolean check(SystemType clientType, String otpCheckSum, OtpCheckForm form);
}
