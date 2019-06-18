package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class AssetContract {

    private final String address;
    private final String name;
    private final String symbol;
    private final String imageUrl;
    private final String description;
    private final String externalLink;

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
