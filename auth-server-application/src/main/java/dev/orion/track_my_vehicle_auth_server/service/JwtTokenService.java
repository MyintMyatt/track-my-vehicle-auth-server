package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.repo.AccountRepo;
import dev.orion.time.DateTimeUtils;
import dev.orion.time.TimeSetting;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private static final String ROLE_KEY = "rol";
    private static final String TOKEN_TYPE_KEY = "typ";
    private static final String FULL_NAME_KEY = "fun";

    private PrivateKey privateKey;

    @Value("${classpath:certs/private-key.pem")
    private Resource privateKeyResource;

    @Value("${app.jwt.token.issuer}")
    private String issuer;

    @Value("${app.jwt.token.access.life}")
    private int accessLifeTime;

    @Value("${app.jwt.token.refresh.life}")
    private int refreshLifeTime;

    private final AccountRepo accountRepo;

    @PostConstruct
    public void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key = new String(Files.readAllBytes(Paths.get(privateKeyResource.getURI())));
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    public String generateToken(TokenType tokenType, Authentication authentication, boolean isWeb) {

        if (null != authentication && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            var now = LocalDateTime.now();
            var builder = Jwts.builder().signWith(privateKey);

            var username = authentication.getName();
            var authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));

            var userId = Long.parseLong(Objects.requireNonNull(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(this::isLong).toList().getFirst()));

            var account = accountRepo.findById(userId).orElse(null);
            if (null != account && null != account.getAccountAccess()) {
                var access = account.getAccountAccess();
                access.setLogin(true);
                access.setLoginAt(now);
                if (null == access.getFirstAccessTime()) {
                    access.setFirstAccessTime(now);
                }
            }

            builder.subject(username);
            builder.issuer(issuer);
            builder.issuedAt(DateTimeUtils.toDate(now));
            builder.expiration(DateTimeUtils.toDate(getExpiration(now, tokenType, isWeb)));

            builder.claim(ROLE_KEY, authorities);
            builder.claim(TOKEN_TYPE_KEY, tokenType);

            if(null != account){
                builder.claim(FULL_NAME_KEY, account.getFullName());
            }

            return builder.compact();
        }
        return null;
    }

    private LocalDateTime getExpiration(LocalDateTime dateTime, TokenType tokenType, boolean isWeb) {
        var timeSetting = (tokenType == TokenType.Access) ? new TimeSetting(ChronoUnit.MINUTES, accessLifeTime) : new TimeSetting(isWeb ? ChronoUnit.MINUTES : ChronoUnit.HOURS, refreshLifeTime);
        return dateTime.plus(timeSetting.value(), timeSetting.unit());
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
