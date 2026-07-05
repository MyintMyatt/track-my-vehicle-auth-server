package dev.orion.track_my_vehicle_auth_server.service;

import dev.orion.auth.constant.LockSettingType;
import dev.orion.auth.entity.LockSetting;
import dev.orion.auth.repo.LockSettingRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LockSettingService {

    private final LockSettingRepo repo;

    public LockSetting findSetting(LockSettingType type) {
        // TODO: implement Resource Not Found Custom Exception
        var setting = repo.findOne(settingQuery(type)).orElseThrow(() -> new RuntimeException("Not found setting"));
        return  setting;
    }

    private Function<CriteriaBuilder, CriteriaQuery<LockSetting>> settingQuery(LockSettingType type){
        return  cb -> {
            var cq = cb.createQuery(LockSetting.class);
            var root = cq.from(LockSetting.class);
            cq.where(
                    cb.equal(root.get("lockSettingType"), type)
            );
            return cq;
        };
    }
}
