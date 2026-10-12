package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.core.domain.common.constant.SystemType;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpRequestForm;

public interface OtpService {
    
    boolean sendPublicOtp(SystemType clientType ,String recipent);
    boolean checkPublicOtp(SystemType clientType, OtpCheckForm form);
    boolean send(SystemType clientType, OtpRequestForm form);
    boolean check(SystemType clientType, String otpCheckSum, OtpCheckForm form);
}
