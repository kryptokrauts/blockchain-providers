package network.arkane.provider.opensea;

import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.domain.AssetContract;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OpenSeaContractToNonFungibleContractMapper {

    private NonFungibleContractTypeMapper contractTypeMapper;

    public OpenSeaContractToNonFungibleContractMapper(NonFungibleContractTypeMapper contractTypeMapper) {
        this.contractTypeMapper = contractTypeMapper;
    }

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
                                  .type(contractTypeMapper.getType(openSeaAssetContract.getAddress()))
                                  .build();
    }

    public List<NonFungibleContract> mapToList(final Collection<? extends AssetContract> openSeaAssetContracts) {
        return openSeaAssetContracts.stream()
                                    .map(this::map)
                                    .collect(Collectors.toList());
    }
}
