package dev.orion.track_my_vehicle_auth_server.service.grpc.auth;

import dev.orion.grpc.auth.private_client.PermissionCheckRequest;
import dev.orion.grpc.auth.private_client.PermissionCheckResponse;
import dev.orion.grpc.auth.public_client.AuthServiceGrpc;
import dev.orion.grpc.auth.public_client.ServiceLoginRequest;
import dev.orion.grpc.auth.public_client.ServiceLoginResponse;
import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.service.AuthService;
import dev.orion.track_my_vehicle_auth_server.service.RoleAndPermissionService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class S2SAuthService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;
    private final RoleAndPermissionService roleAndPermissionService;

    @Override
    public void login(ServiceLoginRequest request, StreamObserver<ServiceLoginResponse> responseObserver) {
       var result =  authService.internalServiceLogin(ClientOrigin.InternalService , request);
       responseObserver.onNext(result);
       responseObserver.onCompleted();
    }

    @Override
    public void checkPermission(PermissionCheckRequest request, StreamObserver<PermissionCheckResponse> responseObserver) {
        responseObserver.onNext(roleAndPermissionService.checkPermission(request));
        responseObserver.onCompleted();
    }
}
