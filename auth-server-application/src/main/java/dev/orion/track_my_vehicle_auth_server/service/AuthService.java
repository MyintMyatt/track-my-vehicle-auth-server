package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.constant.AccessStatus;
import dev.orion.grpc.auth.public_client.ServiceLoginRequest;
import dev.orion.grpc.auth.public_client.ServiceLoginResponse;
import dev.orion.track_my_vehicle_auth_server.constant.ClientOrigin;
import dev.orion.track_my_vehicle_auth_server.constant.TokenType;
import dev.orion.track_my_vehicle_auth_server.dto.request.AuthRequest;
import dev.orion.track_my_vehicle_auth_server.dto.response.CheckEmployeeAccountResponse;
import dev.orion.track_my_vehicle_auth_server.dto.response.LoginResponse;
import dev.orion.track_my_vehicle_auth_server.logs.event.AccountAccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtTokenService tokenService;

    @Value("${app.email.domain.name}")
    private String emailDomainName;

    @Transactional(noRollbackFor = AuthenticationException.class)
    public LoginResponse login(ClientOrigin clientOrigin, AuthRequest request) {
        var accessEvent = new AccountAccessEvent();
        var authentication = authenticationManager.authenticate(request.authentication(clientOrigin.name()));
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenService.generateToken(TokenType.Access, authentication, clientOrigin == ClientOrigin.AdminPortal);
            String refreshToken = tokenService.generateToken(TokenType.Refresh, authentication, clientOrigin == ClientOrigin.AdminPortal);

            var account = accountService.findAccountByUserName(authentication.getName());
            accessEvent = AccountAccessEvent.loginSuccess(authentication.getName(), request.deviceInfo());
            return LoginResponse.builder()
                    .id(account.getId())
                    .username(authentication.getName())
                    .fullName(account.getFullName())
                    .userType(account.getUserType())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException e) {
            accessEvent = AccountAccessEvent.loginFail(authentication.getName(), AccessStatus.from(e), request.deviceInfo());
            throw e;
        } finally {
            eventPublisher.publishEvent(accessEvent);
        }
    }

    @Transactional(noRollbackFor = AuthenticationException.class)
    public ServiceLoginResponse internalServiceLogin(ClientOrigin clientOrigin, ServiceLoginRequest request) {
        var token = UsernamePasswordAuthenticationToken.unauthenticated(ClientOrigin.InternalService.name() + "-" + request.getClientId(), request.getClientSecret());
        var authentication = authenticationManager.authenticate(token);
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenService.generateToken(TokenType.Access, authentication, clientOrigin == ClientOrigin.AdminPortal);
            String refreshToken = tokenService.generateToken(TokenType.Refresh, authentication, clientOrigin == ClientOrigin.AdminPortal);

            return ServiceLoginResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Successfully login")
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .build();

        } catch (AuthenticationException e) {
            throw e;
        }
    }

    public ServiceLoginResponse refreshToken(String token) {
       var tokenParseData = tokenService.parseToken(token);

        var authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken.authenticated(
                tokenParseData.getUsername(),
                null,
                tokenParseData.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList()
        ));

        String accessToken = tokenService.generateToken(TokenType.Access, authentication, false);
        String refreshToken = tokenService.generateToken(TokenType.Refresh, authentication, false);

        return ServiceLoginResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Successfully refresh token")
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build();
    }


    public CheckEmployeeAccountResponse checkAccountByEmail(String email) {
        // Check email with company domain name that is company mail or not
        if (!checkEmailDomainName(email)) {
            throw new RuntimeException("Your email is not company mail.");
        }

        var employee = accountService.findEmployeeByEmail(email);

        if(employee != null){
            return new CheckEmployeeAccountResponse(false, "Account already existed with this email");
        }

        // TODO: send otp mail

        return new CheckEmployeeAccountResponse(true, "Please verify OTP to finish setting up your account.We've already sent OTP to your email.");
    }

    private boolean checkEmailDomainName(String email) {
        var arr = email.split("@");
        String domain = arr[1];
        return emailDomainName.equals(domain);
    }

}
