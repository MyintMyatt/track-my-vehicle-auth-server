package dev.orion.track_my_vehicle_auth_server.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "dev.orion.commons"
})
public class CommonBeanConfiguration {

}
