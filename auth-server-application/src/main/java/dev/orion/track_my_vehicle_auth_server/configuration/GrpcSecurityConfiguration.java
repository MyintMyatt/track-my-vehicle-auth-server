package dev.orion.track_my_vehicle_auth_server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.AuthenticationProcessInterceptor;
import org.springframework.grpc.server.security.GrpcSecurity;

@Configuration
public class GrpcSecurityConfiguration {

    @GlobalServerInterceptor
    public AuthenticationProcessInterceptor grpcSecurityFilterChain(GrpcSecurity grpc) throws Exception {
        return grpc
                .authorizeRequests(req -> req
                        .methods("dev.orion.grpc.auth.public_client.AuthService/*").permitAll()
                        .allRequests().authenticated()
                )
                .build();
    }
}
