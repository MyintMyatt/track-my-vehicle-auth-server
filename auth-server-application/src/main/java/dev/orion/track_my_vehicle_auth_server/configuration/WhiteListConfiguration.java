package dev.orion.track_my_vehicle_auth_server.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
public class WhiteListConfiguration {

    @Value("${app.security.white.list}")
    private List<String> whiteList;
    
}
