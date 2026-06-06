package dev.orion.auth.repo;

import dev.orion.auth.entiy.DriverAccount;
import dev.orion.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAccountRepo extends AbstractRepository<DriverAccount, Long> {
}
