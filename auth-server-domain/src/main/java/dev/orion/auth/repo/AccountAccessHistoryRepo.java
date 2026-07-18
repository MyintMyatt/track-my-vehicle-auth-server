package dev.orion.auth.repo;

import dev.orion.auth.embedded.AccessHistoryPk;
import dev.orion.auth.entity.AccountAccessHistory;
import dev.orion.core.domain.repository.AbstractRepository;

public interface AccountAccessHistoryRepo extends AbstractRepository<AccountAccessHistory, AccessHistoryPk> {
}
