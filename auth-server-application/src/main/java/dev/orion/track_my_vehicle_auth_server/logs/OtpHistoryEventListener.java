package dev.orion.track_my_vehicle_auth_server.logs;

import dev.orion.auth.embedded.OtpHistoryPk;
import dev.orion.auth.entity.OtpHistory;
import dev.orion.auth.repo.OtpHistoryRepo;
import dev.orion.track_my_vehicle_auth_server.logs.event.OtpHistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OtpHistoryEventListener {

    private final OtpHistoryRepo repo;

    @Async
    @EventListener
    @Transactional
    public void saveHistory(OtpHistoryEvent event){
        var history = new OtpHistory();
        var pk = new OtpHistoryPk().pk(event.email());
        history.setOtpHistoryPk(pk);
        history.setOtpHistoryType(event.type());
        history.setUsed(event.isUsed());
    }
}
