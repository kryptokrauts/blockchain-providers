package network.arkane.provider.nonfungible.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NonFungibleAsset {

    private String id;
    private String name;
    private String description;
    private String url;
    private String backgroundColor;
    private String imageUrl;
    private String imagePreviewUrl;
    private String imageThumbnailUrl;
    @Deprecated
    private String animationUrl;
    private List<TypeValue> animationUrls;
    private Boolean fungible;
    private NonFungibleContract contract;
    private List<Attribute> attributes;

    @Builder(toBuilder = true)
    public NonFungibleAsset(final String id,
                            final String name,
                            final String description,
                            final String url,
                            final String backgroundColor,
                            final String imageUrl,
                            final String imagePreviewUrl,
                            final String imageThumbnailUrl,
                            final String animationUrl,
                            final List<TypeValue> animationUrls,
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
        this.animationUrls = animationUrls;
        this.contract = contract;
        this.attributes = attributes;
        this.fungible = fungible != null && fungible;
    }

    public static NonFungibleAsset from(NonFungibleAssetBalance assetBalance) {
        return NonFungibleAsset.builder()
                               .id(assetBalance.getId())
                               .name(assetBalance.getName())
                               .description(assetBalance.getDescription())
                               .url(assetBalance.getUrl())
                               .backgroundColor(assetBalance.getBackgroundColor())
                               .imageUrl(assetBalance.getImageUrl())
                               .imagePreviewUrl(assetBalance.getImagePreviewUrl())
                               .imageThumbnailUrl(assetBalance.getImageThumbnailUrl())
                               .animationUrl(assetBalance.getAnimationUrl())
                               .animationUrls(assetBalance.getAnimationUrls())
                               .fungible(assetBalance.getFungible())
                               .contract(assetBalance.getContract())
                               .attributes(assetBalance.getAttributes())
                               .build();
    }
}
