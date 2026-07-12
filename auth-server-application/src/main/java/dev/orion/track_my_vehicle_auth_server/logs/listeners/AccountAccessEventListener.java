package dev.orion.track_my_vehicle_auth_server.logs.listeners;

import dev.orion.auth.constant.AccessStatus;
import dev.orion.auth.constant.AccessType;
import dev.orion.auth.entity.AccountAccess;
import dev.orion.auth.repo.AccountAccessHistoryRepo;
import dev.orion.track_my_vehicle_auth_server.logs.event.AccountAccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountAccessEventListener {

    private final AccountAccessHistoryRepo accessHistoryRepo;

    @Async@EventListener
    public void handle(AccountAccessEvent event){
        accessHistoryRepo.save(event.history());
        if (event.accessType() == AccessType.Login){
            var access = new AccountAccess();

        }
    }
}
