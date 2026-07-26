package dev.orion.track_my_vehicle_auth_server.startup;

import dev.orion.auth.embedded.UserName;
import dev.orion.auth.entity.Account;
import dev.orion.auth.entity.AdminAccount;
import dev.orion.auth.repo.AccountRepo;
import dev.orion.core.domain.account.constant.AccountStatus;
import dev.orion.core.domain.account.constant.AdminAccountStatus;
import dev.orion.core.domain.account.constant.UserType;
import dev.orion.core.domain.common.constant.SystemType;
import dev.orion.core.domain.transaction.constant.TransactionState;
import dev.orion.track_my_vehicle_auth_server.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepo accountRepo;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Value("${super.admin.name:superadmin}")
    private String superAdminName;

    @Value("${super.admin.name:superadmin@gmail.com}")
    private String superAdminEmail;

    @Value("${super.admin.password:password#123}")
    private String superAdminPassword;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // save super admin
        if(accountService.findSuperAdmin().isEmpty()){
            var acc = new AdminAccount();
            acc.setAdminAccountStatus(AdminAccountStatus.ACTIVE);
            acc.setUserName(UserName.builder().systemType(SystemType.PORTAL).uniqueName(superAdminName).build());
            acc.setPhone("09*********");
            acc.setEmail(superAdminEmail);
            acc.setTransactionState(TransactionState.SUCCESS);
            acc.setPassword(passwordEncoder.encode(superAdminPassword));
            acc.setUserType(UserType.SUPER_ADMIN);
            accountRepo.save(acc);
        }
    }
}
