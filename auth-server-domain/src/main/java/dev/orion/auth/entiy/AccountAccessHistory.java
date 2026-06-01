package dev.orion.auth.entiy;

import dev.orion.auth.constant.AccessStatus;
import dev.orion.auth.embedded.AccessHistoryPk;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "account_access_history")
public class AccountAccessHistory {

    @EmbeddedId
    private AccessHistoryPk accessHistoryPk;

    private AccessType accessType;
    private AccessStatus accessStatus;

    private String accountName;

    private String ipAddress;
    private String device;
    private String deviceId;
    private String remark;
}
