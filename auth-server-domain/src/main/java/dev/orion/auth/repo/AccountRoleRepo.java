package dev.orion.auth.repo;

import dev.orion.auth.entity.AccountRole;
import dev.orion.core.domain.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

public interface AccountRoleRepo extends AbstractRepository<AccountRole, Long> {
}
