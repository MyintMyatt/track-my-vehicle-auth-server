package dev.orion.track_my_vehicle_auth_server.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenParseData {
    private String username;
    private String tokenType;
    private List<String> authorities;
}
