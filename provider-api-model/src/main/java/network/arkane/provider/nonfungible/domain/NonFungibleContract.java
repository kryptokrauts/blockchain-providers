package network.arkane.provider.nonfungible.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class NonFungibleContract {

    private String name;
    private String description;
    private String address;
    private String symbol;
    @JsonAlias("externalUrl")
    private String url;
    @JsonAlias("image")
    private String imageUrl;
    private String media;
    private String type;

}
