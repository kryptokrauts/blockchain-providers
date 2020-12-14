package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Asset {

    private String tokenId;
    private String imageUrl;
    private String imagePreviewUrl;
    private String imageThumbnailUrl;
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
                 @JsonProperty("image_preview_url") final String imagePreviewUrl,
                 @JsonProperty("image_thumbnail_url") final String imageThumbnailUrl,
                 @JsonProperty("background_color") final String backgroundColor,
                 @JsonProperty("name") final String name,
                 @JsonProperty("description") final String description,
                 @JsonProperty("external_link") final String externalLink,
                 @JsonProperty("asset_contract") final AssetContract assetContract,
                 @JsonProperty("owner") final Account owner,
                 @JsonProperty("traits") final List<Trait> traits) {
        this.tokenId = tokenId;
        this.imageUrl = imageUrl;
        this.imagePreviewUrl = imagePreviewUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.backgroundColor = backgroundColor;
        this.name = name;
        this.description = description;
        this.externalLink = externalLink;
        this.assetContract = assetContract;
        this.owner = owner;
        this.traits = traits;
    }
}
