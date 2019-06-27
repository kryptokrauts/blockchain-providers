package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Asset {

    private String tokenId;
    private String imageUrl;
    private final String imageOriginalUrl;
    private String backgroundColor;
    private String name;
    private String description;
    private String externalLink;
    private AssetContract assetContract;
    private Account owner;
    private List<Trait> traits;

    @Builder
    public Asset(@JsonProperty("token_id") final String tokenId,
                 @JsonProperty("image_url") final String imageUrl,
                 @JsonProperty("image_original_url") final String imageOriginalUrl,
                 @JsonProperty("background_color") final String backgroundColor,
                 @JsonProperty("name") final String name,
                 @JsonProperty("description") final String description,
                 @JsonProperty("external_link") final String externalLink,
                 @JsonProperty("asset_contract") final AssetContract assetContract,
                 @JsonProperty("owner") final Account owner,
                 @JsonProperty("traits") final List<Trait> traits) {
        this.tokenId = tokenId;
        this.imageUrl = imageUrl;
        this.imageOriginalUrl = imageOriginalUrl;
        this.backgroundColor = backgroundColor;
        this.name = name;
        this.description = description;
        this.externalLink = externalLink;
        this.assetContract = assetContract;
        this.owner = owner;
        this.traits = traits;
    }
}