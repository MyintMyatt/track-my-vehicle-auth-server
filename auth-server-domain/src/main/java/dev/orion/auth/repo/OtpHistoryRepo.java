package dev.orion.auth.repo;

import dev.orion.auth.embedded.OtpHistoryPk;
import dev.orion.auth.entity.OtpHistory;
import dev.orion.repository.AbstractRepository;

public interface OtpHistoryRepo extends AbstractRepository<OtpHistory, OtpHistoryPk> {
}
