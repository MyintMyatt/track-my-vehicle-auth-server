package dev.orion.auth.embedded;

import dev.orion.commons.exception.auth.OtpException;
import dev.orion.commons.utils.time.DateTimeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import dev.orion.auth.embedded.UserName;

import java.time.LocalDateTime;

@Embeddable
@Data
public class OtpHistoryPk {

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private UserName username;

    private int seq;

    public OtpHistoryPk pk(UserName username){
        var pk = new OtpHistoryPk();
        pk.setIssuedAt(LocalDateTime.now());
        pk.setUsername(username);
        pk.setSeq(++seq);
        return pk;
    }

    public String toOtpKey(){
        var dateTime = DateTimeUtils.yyyyMMddHHmmss(issuedAt);
        return dateTime + "-" + username.toString() + "-" + seq;
    }

    public static OtpHistoryPk fromOtpKey(String key){
        var part = key.split("-");
        if(part.length != 3) {
            throw new OtpException("Invalid OTP Key");
        }
        var username = new UserName();
        username = username.toUserName(part[1]);
        var pk = new OtpHistoryPk();
        pk.setIssuedAt(LocalDateTime.parse(part[0]));
        pk.setUsername(username);
        pk.setSeq(Integer.parseInt(part[2]));
        return pk;
    }
}
