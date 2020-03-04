package network.arkane.provider.nonfungible.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class NonFungibleAsset {

    private String id;
    private String tokenId;
    private String name;
    private String description;
    private String url;
    private String backgroundColor;
    private String imageUrl;
    private String imagePreviewUrl;
    private String imageThumbnailUrl;
    private NonFungibleContract contract;

    @Builder
    public NonFungibleAsset(final String tokenId,
                            final String name,
                            final String description,
                            final String url,
                            final String backgroundColor,
                            final String imageUrl,
                            final String imagePreviewUrl,
                            final String imageThumbnailUrl,
                            final NonFungibleContract contract) {
        this.id = tokenId;
        this.tokenId = tokenId;
        this.name = name;
        this.description = description;
        this.url = url;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.imagePreviewUrl = imagePreviewUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.contract = contract;
    }
}
