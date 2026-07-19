package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.core.domain.account.constant.DriverAccountStatus;
import dev.orion.core.domain.account.constant.EmployeeAccountStatus;
import dev.orion.auth.entity.Account;
import dev.orion.auth.entity.AdminAccount;
import dev.orion.auth.entity.DriverAccount;
import dev.orion.auth.entity.EmployeeAccount;
import dev.orion.auth.repo.AccountRepo;
import dev.orion.auth.repo.AdminAccountRepo;
import dev.orion.auth.repo.DriverAccountRepo;
import dev.orion.auth.repo.EmployeeAccountRepo;
import dev.orion.core.domain.transaction.constant.TransactionState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final EmployeeAccountRepo employeeRepo;
    private final AdminAccountRepo adminRepo;
    private final DriverAccountRepo driverRepo;
    private final AccountRepo accountRepo;

    public Account findById(long id) {
        return accountRepo.findById(id).orElse(null);
    }

    public Account findAccountByUserName(String name) {
        return accountRepo.findOne(cb -> {
            var cq = cb.createQuery(Account.class);
            var root = cq.from(Account.class);
            cq.select(root);
            cq.where(
                    cb.equal(root.get("userName").get("uniqueName"), name),
                    cb.notEqual(root.get("transactionState"), TransactionState.FAIL),
                    cb.isFalse(root.get("permanentLock"))
            );
            return cq;
        }).orElse(null);
    }

    public EmployeeAccount findEmployeeByEmail(String email) {
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
            return cq;
        }).orElse(null);
    }

    public Optional<EmployeeAccount> findEmployeeByEmailOrName(String email, String name) {
        return employeeRepo.findOne(cb -> {
            var cq = cb.createQuery(EmployeeAccount.class);
            var root = cq.from(EmployeeAccount.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.or(
                                    cb.equal(root.get("email"), email),
                                    cb.equal(root.get("userName").get("uniqueName"), name)
                            ),
                            cb.notEqual(root.get("employeeAccountStatus"), EmployeeAccountStatus.CLOSED)
                    )
            );
            return cq;
        });
    }

    public DriverAccount findDriverByPhone(String phone) {
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
            return cq;
        }).orElse(null);
    }

    public Optional<DriverAccount> findDriverByPhoneOrUserName(String phone, String name) {
        return driverRepo.findOne(cb -> {
            var cq = cb.createQuery(DriverAccount.class);
            var root = cq.from(DriverAccount.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.or(
                                    cb.equal(root.get("phone"), phone),
                                    cb.equal(root.get("userName").get("uniqueName"), name)
                            ),
                            cb.notEqual(root.get("driverAccountStatus"), DriverAccountStatus.CLOSED)
                    )
            );
            return cq;
        });
    }

    public AdminAccount findAdminByEmail(String email) {
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
            return cq;
        }).orElse(null);
    }

    public Optional<AdminAccount> findAdminByEmailOrUserName(String email, String name) {
        return adminRepo.findOne(cb -> {
            var cq = cb.createQuery(AdminAccount.class);
            var root = cq.from(AdminAccount.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.or(
                                    cb.equal(root.get("email"), email),
                                    cb.equal(root.get("userName").get("uniqueName"), name)

                            ),
                            cb.notEqual(root.get("adminAccountStatus"), DriverAccountStatus.CLOSED)
                    )
            );
            return cq;
        });
    }



}
