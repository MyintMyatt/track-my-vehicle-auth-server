package dev.orion.auth.entiy;

import dev.orion.auth.constant.OtpHistoryType;
import dev.orion.auth.embedded.OtpHistoryPk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpHistory {

    @EmbeddedId
    private OtpHistoryPk otpHistoryPk;

    @Enumerated(EnumType.STRING)
    private OtpHistoryType otpHistoryType;

    private boolean isUsed = false;
}
