package dev.orion.track_my_vehicle_auth_server.logs.listeners;

import dev.orion.auth.constant.AccessType;
import dev.orion.auth.entity.AccountAccess;
import dev.orion.auth.repo.AccountAccessHistoryRepo;
import dev.orion.track_my_vehicle_auth_server.logs.event.AccountAccessEvent;
import dev.orion.track_my_vehicle_auth_server.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AccountAccessEventListener {

    private final AccountAccessHistoryRepo accessHistoryRepo;
    private final AccountService accountService;

    @Async@EventListener
    @Transactional
    public void handle(AccountAccessEvent event){
        accessHistoryRepo.save(event.history());
        if (event.getAccessType() == AccessType.Login){
           var account = accountService.findAccountByUserName(event.getName());
            if(account != null){
                var access = account.getAccountAccess();
                if(access == null){
                    access = new AccountAccess();
                    access.setAccount(account);
                    account.setAccountAccess(access);
                }
                if(access.getFirstAccessTime() == null){
                    access.setFirstAccessTime(LocalDateTime.now());
                }
                access.setLastAccessTime(event.history().getAccessHistoryPk().getAccessAt());
                access.setLogin(true);
                access.setLoginAt(LocalDateTime.now());
            }
        }
    }
}
