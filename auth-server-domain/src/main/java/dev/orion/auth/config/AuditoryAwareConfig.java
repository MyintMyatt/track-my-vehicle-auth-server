package dev.orion.auth.config;

import dev.orion.auth.embedded.Auditor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditoryAwareConfig implements AuditorAware<Auditor> {
    @Override
    public Optional<Auditor> getCurrentAuditor() {

        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        return Optional.of(
                Auditor.builder()
                        .userName(authentication.getName())
                        .fullName(String.valueOf(authentication.getDetails()))
                        .build()
        );
    }
}
