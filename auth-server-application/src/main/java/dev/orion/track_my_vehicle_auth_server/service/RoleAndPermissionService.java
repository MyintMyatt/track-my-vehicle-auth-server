package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.entity.AccountPermission;
import dev.orion.auth.repo.AccountPermissionRepo;
import dev.orion.commons.exception.ExceptionMessageHolder;
import dev.orion.commons.exception.ResourceNotFoundException;
import dev.orion.grpc.auth.client.Action;
import dev.orion.grpc.auth.client.PermissionCheckRequest;
import dev.orion.grpc.auth.client.PermissionCheckResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RoleAndPermissionService {

    private final AccountService accountService;
    private final AccountPermissionRepo permissionRepo;

    @Transactional
    public PermissionCheckResponse checkPermission(PermissionCheckRequest request){

        var account = accountService.findAccountByUserName(request.getUsername());
        var permission = permissionRepo.findOne(fetchPermission(account.getAccountRole().getId(), request.getResource())).orElseThrow(()->
                new ResourceNotFoundException(new ExceptionMessageHolder(new ExceptionMessageHolder.Message("business.entity.notExisted", new Object[]{"Role"})))
        );

        var isGrant = isGranted(request.getAction(), permission);
        return PermissionCheckResponse.newBuilder()
                .setIsGranted(isGrant)
                .setMessage(isGrant ? "that user has permission for this resource" : "this user has no permission for this resource")
                .build();
    }

    private Function<CriteriaBuilder, CriteriaQuery<AccountPermission>> fetchPermission(long roleId, String resource){
        return cb -> {
            var cq = cb.createQuery(AccountPermission.class);
            var root = cq.from(AccountPermission.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.equal(root.get("role").get("id"), roleId),
                            cb.equal(root.get("resources").get("resource"), resource)
                    )
            );
            return cq;
        };
    }

    private boolean isGranted(Action action, AccountPermission permission){
        return switch (action){
            case request -> permission.isRequest();
            case create -> permission.isCreate();
            case approve -> permission.isApprove();
            case reject -> permission.isReject();
            case edit -> permission.isEdit();
            case view -> permission.isView();
            case export -> permission.isExport();
            case delete -> permission.isDelete();
            default -> false;
        };
    }
}
