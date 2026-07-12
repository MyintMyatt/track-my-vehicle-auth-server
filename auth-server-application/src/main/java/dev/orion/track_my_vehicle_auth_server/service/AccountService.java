package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.account.constant.DriverAccountStatus;
import dev.orion.account.constant.EmployeeAccountStatus;
import dev.orion.auth.entity.AdminAccount;
import dev.orion.auth.entity.DriverAccount;
import dev.orion.auth.entity.EmployeeAccount;
import dev.orion.auth.repo.AccountRepo;
import dev.orion.auth.repo.AdminAccountRepo;
import dev.orion.auth.repo.DriverAccountRepo;
import dev.orion.auth.repo.EmployeeAccountRepo;
import dev.orion.commons.exception.BusinessException;
import dev.orion.commons.exception.ExceptionMessageHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final EmployeeAccountRepo employeeRepo;
    private final AdminAccountRepo adminRepo;
    private final DriverAccountRepo driverRepo;

    public EmployeeAccount findEmployeeByEmail(String email){
        return employeeRepo.findOne(cb -> {
            var cq = cb.createQuery(EmployeeAccount.class);
            var root = cq.from(EmployeeAccount.class);
            cq.select(root);
            cq.where(
                    cb.and(
                          cb.equal(root.get("email"), email),
                          cb.notEqual(root.get("employeeAccountStatus"), EmployeeAccountStatus.CLOSED)
                    )
            );
            return  cq;
        }).orElse(null);
    }

    public DriverAccount findDriverByPhone(String phone){
        return driverRepo.findOne(cb -> {
            var cq = cb.createQuery(DriverAccount.class);
            var root = cq.from(DriverAccount.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.equal(root.get("phone"), phone),
                            cb.notEqual(root.get("driverAccountStatus"), DriverAccountStatus.CLOSED)
                    )
            );
            return  cq;
        }).orElse(null);
    }

    public AdminAccount findAdminByEmail(String email){
        return adminRepo.findOne(cb -> {
            var cq = cb.createQuery(AdminAccount.class);
            var root = cq.from(AdminAccount.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.equal(root.get("email"), email),
                            cb.notEqual(root.get("adminAccountStatus"), DriverAccountStatus.CLOSED)
                    )
            );
            return  cq;
        }).orElse(null);
    }

}
