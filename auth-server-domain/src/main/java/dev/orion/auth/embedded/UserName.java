package dev.orion.auth.embedded;

import dev.orion.core.domain.common.constant.SystemType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class UserName {
    @Column(nullable = false)
    private SystemType systemType;
    @Column(unique = true, nullable = false)
    private String uniqueName; // unique user name

    public String toString(){
        return systemType.name() + "-@" + uniqueName.toLowerCase();
    }

    public UserName toUserName(String name){
        var key = new UserName();
        var arr = name.split("-");
        key.setSystemType(SystemType.valueOf(arr[0]));
        key.setUniqueName(arr[1]);
        return key;
    }
}
