package network.arkane.provider.nonfungible.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class NonFungibleAsset {

    private String id;
    private String name;
    private String description;
    private String url;
    private String backgroundColor;
    private String imageUrl;
    private String imagePreviewUrl;
    private String imageThumbnailUrl;
    private String animationUrl;
    private Boolean fungible;
    private NonFungibleContract contract;
    private List<Attribute> attributes;

    @Builder
    public NonFungibleAsset(final String id,
                            final String name,
                            final String description,
                            final String url,
                            final String backgroundColor,
                            final String imageUrl,
                            final String imagePreviewUrl,
                            final String imageThumbnailUrl,
                            final String animationUrl,
                            final Boolean fungible,
                            final NonFungibleContract contract,
                            final List<Attribute> attributes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.imagePreviewUrl = imagePreviewUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.animationUrl = animationUrl;
        this.contract = contract;
        this.attributes = attributes;
        this.fungible = fungible != null && fungible;
    }
}
