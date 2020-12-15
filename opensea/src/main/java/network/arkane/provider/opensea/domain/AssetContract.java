package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssetContract {

    private String address;
    private String name;
    private String symbol;
    private String imageUrl;
    private String description;
    private String externalLink;

    @Builder
    public AssetContract(@JsonProperty("address") final String address,
                         @JsonProperty("name") final String name,
                         @JsonProperty("symbol") final String symbol,
                         @JsonProperty("image_url") final String imageUrl,
                         @JsonProperty("description") final String description,
                         @JsonProperty("external_link") final String externalLink) {
        this.address = address;
        this.name = name;
        this.symbol = symbol;
        this.imageUrl = imageUrl;
        this.description = description;
        this.externalLink = externalLink;
    }
}
