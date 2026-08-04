package dev.orion.track_my_vehicle_auth_server.startup;

import dev.orion.auth.embedded.UserName;
import dev.orion.auth.entity.AdminAccount;
import dev.orion.auth.entity.InternalServiceClient;
import dev.orion.auth.repo.AccountRepo;
import dev.orion.auth.repo.InternalServiceClientRepo;
import dev.orion.core.domain.account.constant.AdminAccountStatus;
import dev.orion.core.domain.account.constant.UserType;
import dev.orion.core.domain.common.constant.SystemType;
import dev.orion.core.domain.transaction.constant.TransactionState;
import dev.orion.track_my_vehicle_auth_server.service.AccountService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepo accountRepo;
    private final InternalServiceClientRepo clientRepo;
    private final ObjectMapper objectMapper;
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

        // save service client internal-service-clients-uat.json
        try{
            var inputStream = new ClassPathResource("internal-service-clients-uat.json").getInputStream();
            var wrapper = objectMapper.readValue(inputStream, ClientJsonWrapper.class);
            List<InternalServiceClient> entitiesToSave = new ArrayList<>();

            for (ClientJsonWrapper.ClientItemDto dto : wrapper.getClients()) {

                var clientEntity = new InternalServiceClient();
                clientEntity.setClientId(dto.getClientId());
                clientEntity.setClientSecret(passwordEncoder.encode(dto.getClientSecret()));
                clientEntity.setServiceName(dto.getServiceName());
                entitiesToSave.add(clientEntity);
            }

//            clientRepo.saveAll(entitiesToSave);
        }catch (Exception e){
            log.error("Error : {}" , e.getMessage(), e);
        }
    }
}

@Data
class ClientJsonWrapper {
    private List<ClientItemDto> clients;
    @Data
    public static class ClientItemDto {
        private String clientId;
        private String clientSecret;
        private String serviceName;
        private Long role;
    }
}

