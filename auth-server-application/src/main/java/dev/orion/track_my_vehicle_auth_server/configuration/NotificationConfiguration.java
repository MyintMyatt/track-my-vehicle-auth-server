package dev.orion.track_my_vehicle_auth_server.configuration;

import dev.orion.commons.client.notification.grpc.NotificationClient;
import dev.orion.commons.client.notification.grpc.impl.NotificationClientImpl;
import dev.orion.grpc.notification.OtpGrpcServiceGrpc;
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
    OtpGrpcServiceGrpc.OtpGrpcServiceStub otpGrpcServiceStub(ManagedChannel channel){
        return OtpGrpcServiceGrpc.newStub(channel);
    }

    @Bean
    NotificationClient notificationClient(OtpGrpcServiceGrpc.OtpGrpcServiceStub stub){
        return new NotificationClientImpl(stub);
    }
}
