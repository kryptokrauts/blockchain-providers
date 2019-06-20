package network.arkane.provider.nonfungable;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.NonFungibleGateway;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.opensea.OpenSeaGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EthereumNonFungibleGateway implements NonFungibleGateway {

    private final OpenSeaGateway openSeaGateway;
    private final OpenSeaAssetToNonFungibleAssetMapper mapper;

    public EthereumNonFungibleGateway(final OpenSeaGateway openSeaGateway, final OpenSeaAssetToNonFungibleAssetMapper mapper) {
        this.openSeaGateway = openSeaGateway;
        this.mapper = mapper;
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }

    @Override
    public List<NonFungibleAsset> listNonFungibles(final String walletId, final String... contractAddresses) {
        return mapper.mapToList(openSeaGateway.listAssets(walletId, contractAddresses));
    }
}
