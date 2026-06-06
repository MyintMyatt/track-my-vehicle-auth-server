package dev.orion.auth.repo;

import dev.orion.auth.entiy.AdminAccount;
import dev.orion.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAccountRepo extends AbstractRepository<AdminAccount, Long> {
}
