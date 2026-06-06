package dev.orion.auth.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDateTime;

@Embeddable
@Data
public class OtpHistoryPk {

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private String email;

    private int seq;
}
