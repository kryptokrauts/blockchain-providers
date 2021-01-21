package network.arkane.provider.opensea;

import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.Trait;
import network.arkane.provider.opensea.domain.Asset;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OpenSeaAssetToNonFungibleAssetMapper {

    private final OpenSeaContractToNonFungibleContractMapper contractMapper;

    public OpenSeaAssetToNonFungibleAssetMapper(final OpenSeaContractToNonFungibleContractMapper contractMapper) {
        this.contractMapper = contractMapper;
    }

    public NonFungibleAsset map(final Asset openSeaAsset) {
        if (openSeaAsset == null) {
            return null;
        }

        return NonFungibleAsset.builder()
                               .tokenId(openSeaAsset.getTokenId())
                               .name(openSeaAsset.getName())
                               .backgroundColor(openSeaAsset.getBackgroundColor())
                               .description(openSeaAsset.getDescription())
                               .imageUrl(openSeaAsset.getImageUrl())
                               .imagePreviewUrl(openSeaAsset.getImagePreviewUrl())
                               .imageThumbnailUrl(openSeaAsset.getImageThumbnailUrl())
                               .url(openSeaAsset.getExternalLink())
                               .contract(contractMapper.map(openSeaAsset.getAssetContract()))
                               .fungible(false)
                               .attributes(CollectionUtils.emptyIfNull(openSeaAsset.getTraits())
                                                          .stream()
                                                          .map(trait -> Trait.builder()
                                                                             .displayType(trait.getDisplayType())
                                                                             .traitCount(trait.getTraitCount())
                                                                             .traitType(trait.getTraitType())
                                                                             .value(trait.getValue())
                                                                             .build()
                                                              ).collect(Collectors.toList())
                                          )
                               .build();
    }

    public List<NonFungibleAsset> mapToList(final Collection<? extends Asset> openSeaAssets) {
        return openSeaAssets.stream()
                            .map(this::map)
                            .collect(Collectors.toList());
    }
}
