package network.arkane.provider.nonfungable;

import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.domain.AssetContract;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenSeaContractToNonFungibleContractMapper {

    public NonFungibleContract map(final AssetContract openSeaAssetContract) {
        if (openSeaAssetContract == null) {
            return null;
        }

        return NonFungibleContract.builder()
                                  .address(openSeaAssetContract.getAddress())
                                  .description(openSeaAssetContract.getDescription())
                                  .imageUrl(openSeaAssetContract.getImageUrl())
                                  .name(openSeaAssetContract.getName())
                                  .symbol(openSeaAssetContract.getSymbol())
                                  .url(openSeaAssetContract.getExternalLink())
                                  .build();
    }

    public List<NonFungibleContract> mapToList(final Collection<? extends AssetContract> openSeaAssetContracts) {
        return openSeaAssetContracts.stream()
                                    .map(this::map)
                                    .collect(Collectors.toList());
    }
}
