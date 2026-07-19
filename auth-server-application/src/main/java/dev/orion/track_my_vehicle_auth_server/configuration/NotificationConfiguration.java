package dev.orion.track_my_vehicle_auth_server.configuration;

import dev.orion.commons.client.notification.grpc.NotificationClient;
import dev.orion.commons.client.notification.grpc.impl.NotificationClientImpl;
import dev.orion.grpc.notification.OtpServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfiguration {

    @Bean(destroyMethod = "shutdown")
    ManagedChannel managedChannel(){
        return ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .build();
    }

    @Bean
    OtpServiceGrpc.OtpServiceStub otpGrpcServiceStub(ManagedChannel channel){
        return OtpServiceGrpc.newStub(channel);
    }

    @Bean
    NotificationClient notificationClient(OtpServiceGrpc.OtpServiceStub stub){
        return new NotificationClientImpl(stub);
    }
}
