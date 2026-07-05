package dev.orion.track_my_vehicle_auth_server.utils;

import java.security.SecureRandom;


public class OtpCodeGeneratorUtils {

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    public static String generate(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return  sb.toString();
    }

}
