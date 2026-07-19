package dev.orion.track_my_vehicle_auth_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TrackMyVehicleAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackMyVehicleAuthServerApplication.class, args);
	}

}
