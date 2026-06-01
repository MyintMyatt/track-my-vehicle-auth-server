package dev.orion.auth.entiy;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "account_access")
public class AccountAccess {

    @Id
    private long accountId;

    @MapsId
    @OneToOne(optional = false)
    private Account account;

    private LocalDateTime firstAccessTime;
    private LocalDateTime lastAccessTime;

    private boolean login = false;
    private LocalDateTime loginAt;
    private LocalDateTime logoutAt;
}
