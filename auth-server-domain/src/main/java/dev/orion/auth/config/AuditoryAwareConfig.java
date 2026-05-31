package dev.orion.auth.config;

import dev.orion.auth.embedded.Auditor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditoryAwareConfig implements AuditorAware<Auditor> {
    @Override
    public Optional<Auditor> getCurrentAuditor() {
        var context = SecurityContextHolder.getContext();

        if (null != context) {
            var authentication = context.getAuthentication();
            if (null != authentication && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                return Optional.of(
                        Auditor.builder()
                                .userName(authentication.getName())
                                .fullName(Optional.ofNullable(authentication.getDetails()).toString())
                                .build()
                );
            }
        }
        return Optional.empty();

    }
}
