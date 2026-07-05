package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.account.constant.EmployeeAccountStatus;
import dev.orion.auth.entity.EmployeeAccount;
import dev.orion.auth.repo.AccountRepo;
import dev.orion.auth.repo.DriverAccountRepo;
import dev.orion.auth.repo.EmployeeAccountRepo;
import dev.orion.track_my_vehicle_auth_server.dto.input.AuthRequest;
import dev.orion.track_my_vehicle_auth_server.dto.output.CheckEmployeeAccountResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepo accountRepo;
    private final EmployeeAccountRepo employeeRepo;
    private final DriverAccountRepo driverRepo;

    @Value("${app.email.domain.name}")
    private String emailDomainName;

    public Object login(AuthRequest request) {
        return null;
    }

    public CheckEmployeeAccountResponse checkAccountByEmail(String email) {

        // Check email with company domain name that is company mail or not
        if(!checkEmailDomainName(email)){
            throw new RuntimeException("Your email is not company mail.");
        }

        var employee = employeeRepo.findOne(findEmployeeAccountByEmail(email)).orElse(null);
        return new CheckEmployeeAccountResponse(employee == null);

    }

    private boolean checkEmailDomainName(String email){
        var arr = email.split("@");
        String domain = arr[1];
        return emailDomainName.equals(domain);
    }

    private Function<CriteriaBuilder, CriteriaQuery<EmployeeAccount>> findEmployeeAccountByEmail(String email){
        return cb -> {
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
        };
    }
}
