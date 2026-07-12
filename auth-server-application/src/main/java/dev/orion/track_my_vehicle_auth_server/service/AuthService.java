package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.dto.input.AuthRequest;
import dev.orion.track_my_vehicle_auth_server.dto.output.CheckEmployeeAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    @Value("${app.email.domain.name}")
    private String emailDomainName;

    public Object login(ClientOrigin clientOrigin, AuthRequest request) {
        var authentication = authenticationManager.authenticate(request.authentication(clientOrigin.name()));
        return null;
    }

    public CheckEmployeeAccountResponse checkAccountByEmail(String email) {
        // Check email with company domain name that is company mail or not
        if(!checkEmailDomainName(email)){
            throw new RuntimeException("Your email is not company mail.");
        }

        var employee = accountService.findEmployeeByEmail(email);
        return new CheckEmployeeAccountResponse(employee == null);
    }

    private boolean checkEmailDomainName(String email){
        var arr = email.split("@");
        String domain = arr[1];
        return emailDomainName.equals(domain);
    }

}
