package network.arkane.provider.nonfungible.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NonFungibleAssetBalance {

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
    private BigInteger maxSupply;
    private NonFungibleContract contract;
    private List<Attribute> attributes;
    private BigInteger balance;

    @Builder(toBuilder = true)
    public NonFungibleAssetBalance(final String id,
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
                                   final BigInteger maxSupply,
                                   final NonFungibleContract contract,
                                   final List<Attribute> attributes,
                                   final BigInteger balance) {
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
        this.maxSupply = maxSupply;
        this.attributes = attributes;
        this.fungible = fungible != null && fungible;
        this.balance = balance == null ? BigInteger.ZERO : balance;
    }

    public static NonFungibleAssetBalance from(NonFungibleAsset asset,
                                               BigInteger balance) {
        return NonFungibleAssetBalance.builder()
                                      .id(asset.getId())
                                      .name(asset.getName())
                                      .description(asset.getDescription())
                                      .url(asset.getUrl())
                                      .backgroundColor(asset.getBackgroundColor())
                                      .imageUrl(asset.getImageUrl())
                                      .imagePreviewUrl(asset.getImagePreviewUrl())
                                      .imageThumbnailUrl(asset.getImageThumbnailUrl())
                                      .animationUrl(asset.getAnimationUrl())
                                      .animationUrls(asset.getAnimationUrls())
                                      .fungible(asset.getFungible())
                                      .contract(asset.getContract())
                                      .attributes(asset.getAttributes())
                                      .balance(balance)
                                      .build();
    }


}
