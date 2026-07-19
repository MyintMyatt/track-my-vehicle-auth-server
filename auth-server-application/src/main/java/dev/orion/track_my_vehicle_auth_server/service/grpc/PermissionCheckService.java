package dev.orion.track_my_vehicle_auth_server.service.grpc;

import dev.orion.grpc.auth.client.PermissionCheckRequest;
import dev.orion.grpc.auth.client.PermissionCheckResponse;
import dev.orion.grpc.auth.client.PermissionCheckServiceGrpc;
import dev.orion.track_my_vehicle_auth_server.service.RoleAndPermissionService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class PermissionCheckService extends PermissionCheckServiceGrpc.PermissionCheckServiceImplBase {

    private final RoleAndPermissionService roleAndPermissionService;
    @Override
    public void checkPermission(PermissionCheckRequest request, StreamObserver<PermissionCheckResponse> responseObserver) {
        responseObserver.onNext(roleAndPermissionService.checkPermission(request));
        responseObserver.onCompleted();
    }
}
