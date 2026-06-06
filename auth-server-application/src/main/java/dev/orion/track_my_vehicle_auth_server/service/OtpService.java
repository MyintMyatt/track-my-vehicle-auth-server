package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.track_my_vehicle_auth_server.dto.input.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.input.OtpRequestForm;

public interface OtpService {

    boolean send(OtpRequestForm form);
    String check(OtpCheckForm form);

}
