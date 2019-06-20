package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class Account {

    private User user;
    private String profileImgUrl;
    private String address;
    private String config;

    @Builder
    public Account(@JsonProperty("user") final User user,
                   @JsonProperty("profile_img_url") final String profileImgUrl,
                   @JsonProperty("address") final String address,
                   @JsonProperty("config") final String config) {
        this.user = user;
        this.profileImgUrl = profileImgUrl;
        this.address = address;
        this.config = config;
    }
}
