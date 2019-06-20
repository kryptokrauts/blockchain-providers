package network.arkane.provider.nonfungable;

import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.AssetContract;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenSeaAssetToNonFungibleAssetMapper {

    public NonFungibleAsset map(final Asset openSeaAsset) {
        if (openSeaAsset == null) {
            return null;
        }

        final AssetContract assetContract = openSeaAsset.getAssetContract();
        return NonFungibleAsset.builder()
                               .id(openSeaAsset.getTokenId())
                               .name(openSeaAsset.getName())
                               .backgroundColor(openSeaAsset.getBackgroundColor())
                               .description(openSeaAsset.getDescription())
                               .imageUrl(openSeaAsset.getImageOriginalUrl())
                               .url(openSeaAsset.getExternalLink())
                               .owner(openSeaAsset.getOwner() != null ? openSeaAsset.getOwner().getAddress() : null)
                               .contract(NonFungibleContract.builder()
                                                            .address(assetContract.getAddress())
                                                            .description(assetContract.getDescription())
                                                            .imageUrl(assetContract.getImageUrl())
                                                            .name(assetContract.getName())
                                                            .symbol(assetContract.getSymbol())
                                                            .url(assetContract.getExternalLink())
                                                            .build())
                               .build();
    }

    public List<NonFungibleAsset> mapToList(final Collection<? extends Asset> openSeaAssets) {
        return openSeaAssets.stream()
                            .map(this::map)
                            .collect(Collectors.toList());
    }
}
