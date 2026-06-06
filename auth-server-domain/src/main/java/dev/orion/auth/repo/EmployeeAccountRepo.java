package dev.orion.auth.repo;

import dev.orion.auth.entiy.EmployeeAccount;
import dev.orion.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAccountRepo extends AbstractRepository<EmployeeAccount, Long> {
}
