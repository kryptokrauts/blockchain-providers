package network.arkane.provider.opensea;

import network.arkane.provider.nonfungible.domain.Attribute;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.Trait;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
                               .id(openSeaAsset.getTokenId())
                               .name(openSeaAsset.getName())
                               .backgroundColor(openSeaAsset.getBackgroundColor())
                               .description(openSeaAsset.getDescription())
                               .imageUrl(openSeaAsset.getImageUrl())
                               .imagePreviewUrl(openSeaAsset.getImagePreviewUrl())
                               .imageThumbnailUrl(openSeaAsset.getImageThumbnailUrl())
                               .animationUrl(openSeaAsset.getAnimationUrl())
                               .url(openSeaAsset.getExternalLink())
                               .contract(contractMapper.map(openSeaAsset.getAssetContract()))
                               .fungible(false)
                               .attributes(CollectionUtils.emptyIfNull(openSeaAsset.getTraits())
                                                          .stream()
                                                          .map(trait -> Attribute.builder()
                                                                                 .displayType(trait.getDisplayType())
                                                                                 .traitCount(trait.getTraitCount())
                                                                                 .name(trait.getTraitType())
                                                                                 .type(mapType(trait))
                                                                                 .value(mapValue(trait))
                                                                                 .maxValue(trait.getMaxValue())
                                                                                 .build()
                                                              ).collect(Collectors.toList())
                                          )
                               .build();
    }

    private String mapValue(Trait trait) {
        return StringUtils.isNotBlank(trait.getValue()) && "boost_percentage".equalsIgnoreCase(trait.getDisplayType())
               ? trait.getValue() + "%"
               : trait.getValue();
    }

    private String mapType(Trait trait) {
        if (StringUtils.isNotBlank(trait.getDisplayType())) {
            if ("data".equalsIgnoreCase(trait.getDisplayType())) {
                return "property";
            } else if ("number".equalsIgnoreCase(trait.getDisplayType())) {
                return "stat";
            } else if ("boost_number".equalsIgnoreCase(trait.getDisplayType())) {
                return "boost";
            } else if ("boost_percentage".equalsIgnoreCase(trait.getDisplayType())) {
                return "boost";
            }
        }
        return "property";
    }

    public List<NonFungibleAsset> mapToList(final Collection<? extends Asset> openSeaAssets) {
        return openSeaAssets.stream()
                            .map(this::map)
                            .collect(Collectors.toList());
    }
}
