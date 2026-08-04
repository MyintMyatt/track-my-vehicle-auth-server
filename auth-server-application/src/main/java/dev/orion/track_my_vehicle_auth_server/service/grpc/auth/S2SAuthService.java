package dev.orion.track_my_vehicle_auth_server.service.grpc.auth;

import dev.orion.grpc.auth.public_client.AuthPublicServiceGrpc;
import dev.orion.grpc.auth.public_client.ServiceLoginRequest;
import dev.orion.grpc.auth.public_client.ServiceLoginResponse;
import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.service.AuthService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class S2SAuthService extends AuthPublicServiceGrpc.AuthPublicServiceImplBase {

    private final AuthService authService;

    @Override
    public void s2sLogin(ServiceLoginRequest request, StreamObserver<ServiceLoginResponse> responseObserver) {
       var result =  authService.internalServiceLogin(ClientOrigin.InternalService , request);
       responseObserver.onNext(result);
       responseObserver.onCompleted();
    }

}
