package dev.orion.track_my_vehicle_auth_server.service.grpc;

import dev.orion.auth.embedded.UserName;
import dev.orion.auth.entity.EmployeeAccount;
import dev.orion.auth.repo.EmployeeAccountRepo;
import dev.orion.core.domain.account.constant.EmployeeAccountStatus;
import dev.orion.core.domain.account.constant.UserType;
import dev.orion.core.domain.common.constant.SystemType;
import dev.orion.core.domain.transaction.constant.TransactionState;
import dev.orion.grpc.employee.EmployeeRegisterServiceGrpc;
import dev.orion.grpc.employee.RegisterRequest;
import dev.orion.grpc.employee.RegisterResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class EmployeeRegisterService extends EmployeeRegisterServiceGrpc.EmployeeRegisterServiceImplBase {

    private final EmployeeAccountRepo repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        log.info("============GRPC: receive register request from client");
        var account = new EmployeeAccount();
        var username = new UserName();
        username.setSystemType(SystemType.EMPLOYEE);
        username.setUniqueName(request.getUsername());
        account.setUserName(username);
        account.setUserType(UserType.EMPLOYEE);
        account.setFullName(request.getFullName());
        account.setPhone(request.getPhone());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setTransactionState(TransactionState.SUCCESS);
        account.setEmployeeAccountStatus(EmployeeAccountStatus.ACTIVE);
        repo.save(account);

        responseObserver.onNext(RegisterResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Employee register successful.")
                        .build());
        responseObserver.onCompleted();
    }
}
