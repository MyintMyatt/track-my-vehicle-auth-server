package dev.orion.auth.repo;

import dev.orion.auth.entiy.Account;
import dev.orion.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends AbstractRepository<Account, Long> {
}
