package dev.orion.track_my_vehicle_auth_server.dto.output;

import dev.orion.account.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private long id;
    private String username;
    private UserType userType;
    private String fullName;
    private String accessToken;
    private String refreshToken;
}
