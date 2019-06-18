package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class User {

    private String userName;

    @Builder
    public User(@JsonProperty("user_name") final String userName) {
        this.userName = userName;
    }
}
