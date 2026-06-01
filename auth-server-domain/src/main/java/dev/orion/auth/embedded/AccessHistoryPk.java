package dev.orion.auth.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Embeddable
public class AccessHistoryPk {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime accessAt;
}
