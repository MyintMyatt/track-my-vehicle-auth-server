package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.repo.AccountRepo;
import dev.orion.track_my_vehicle_auth_server.common.TokenType;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private PrivateKey privateKey;

    @Value("${classpath:certs/private-key.pem")
    private Resource privateKeyResource;

    private final AccountRepo accountRepo;

    @PostConstruct
    public void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key = new String(Files.readAllBytes(Paths.get(privateKeyResource.getURI())));
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    private String generateToken(TokenType tokenType, Authentication authentication) {

        if (null != authentication && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            var builder = Jwts.builder().signWith(privateKey);

            var username = authentication.getName();
            var authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));

            var userId = Long.parseLong(
                    Objects.requireNonNull(authentication
                            .getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).filter(this::isLong).toList().getFirst())
            );

            var account = accountRepo.findById(userId).orElse(null);
            if(null != account && null != account.getAccountAccess()){

            }
            return null;
        }

        return null;
    }

    private boolean isLong(String authority) {
        try {
            Long.parseLong(authority);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
