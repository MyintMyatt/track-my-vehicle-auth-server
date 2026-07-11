package dev.orion.auth.embedded;

import dev.orion.commons.exception.auth.OtpException;
import dev.orion.commons.utils.time.DateTimeUtils;
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

    public OtpHistoryPk pk(String email){
        var pk = new OtpHistoryPk();
        pk.setIssuedAt(LocalDateTime.now());
        pk.setEmail(email);
        pk.setSeq(++seq);
        return pk;
    }

    public String toOtpKey(){
        var dateTime = DateTimeUtils.yyyyMMddHHmmss(issuedAt);
        return dateTime + "-" + email + "-" + seq;
    }

    public static OtpHistoryPk fromOtpKey(String key){
        var part = key.split("-");
        if(part.length != 3) {
            throw new OtpException("Invalid OTP Key");
        }
        var pk = new OtpHistoryPk();
        pk.setIssuedAt(LocalDateTime.parse(part[0]));
        pk.setEmail(part[1]);
        pk.setSeq(Integer.parseInt(part[2]));
        return pk;
    }
}
