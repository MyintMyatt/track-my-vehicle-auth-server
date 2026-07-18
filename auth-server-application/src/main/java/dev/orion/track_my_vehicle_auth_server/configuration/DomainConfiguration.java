package dev.orion.track_my_vehicle_auth_server.configuration;

import dev.orion.core.domain.repository.AbstractRepositoryImpl;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = {"dev.orion.auth.repo",},
        repositoryBaseClass = AbstractRepositoryImpl.class
)
@EntityScan(basePackages = {"dev.orion.auth.entity"})
public class DomainConfiguration {
}

