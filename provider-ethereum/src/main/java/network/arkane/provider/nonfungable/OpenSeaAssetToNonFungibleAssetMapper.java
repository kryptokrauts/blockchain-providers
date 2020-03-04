package network.arkane.provider.nonfungable;

import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.opensea.domain.Asset;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
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
                               .build();
    }

    public List<NonFungibleAsset> mapToList(final Collection<? extends Asset> openSeaAssets) {
        return openSeaAssets.stream()
                            .map(this::map)
                            .collect(Collectors.toList());
    }
}
