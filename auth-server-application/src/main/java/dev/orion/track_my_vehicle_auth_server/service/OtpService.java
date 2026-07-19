package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.track_my_vehicle_auth_server.dto.request.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.request.OtpRequestForm;

public interface OtpService {

    boolean send(OtpRequestForm form);
    boolean check(String otpCheckSum, OtpCheckForm form);

}
