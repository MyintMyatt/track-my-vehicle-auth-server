package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.track_my_vehicle_auth_server.dto.input.OtpCheckForm;
import dev.orion.track_my_vehicle_auth_server.dto.input.OtpRequestForm;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService{

    @Override
    public boolean send(OtpRequestForm form) {
        return false;
    }

    @Override
    public String check(OtpCheckForm form) {
        return "";
    }
}
