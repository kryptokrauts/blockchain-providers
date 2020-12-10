package network.arkane.provider.nonfungable;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.azrael.dto.token.erc1155.Erc1155TokenBalances;
import network.arkane.blockchainproviders.azrael.dto.token.erc721.Erc721TokenBalances;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.EthereumContractService;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.nonfungible.AzraelNonFungibleStrategy;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.opensea.NonFungibleContractTypeMapper;
import network.arkane.provider.opensea.OpenSeaAssetToNonFungibleAssetMapper;
import network.arkane.provider.opensea.OpenSeaContractToNonFungibleContractMapper;
import network.arkane.provider.opensea.OpenSeaGateway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "ethereum.nonfungible.strategy", havingValue = "mixed")
public class EthereumMixedNonFungibleStrategy extends AzraelNonFungibleStrategy {


    private final OpenSeaContractToNonFungibleContractMapper contractMapper;
    private OpenSeaGateway ethereumOpenSeaGateway;
    private final OpenSeaAssetToNonFungibleAssetMapper mapper;

    public EthereumMixedNonFungibleStrategy(
            EthereumWeb3JGateway ethereumWeb3JGateway,
            AzraelClient ethereumAzraelClient,
            EthereumContractService ethereumContractService,
            OpenSeaGateway ethereumOpenSeaGateway,
            Optional<CacheManager> cacheManager) {
        super(ethereumAzraelClient, ethereumContractService, cacheManager);
        this.contractMapper = new OpenSeaContractToNonFungibleContractMapper(new NonFungibleContractTypeMapper(ethereumWeb3JGateway));
        this.ethereumOpenSeaGateway = ethereumOpenSeaGateway;
        this.mapper = new OpenSeaAssetToNonFungibleAssetMapper(this.contractMapper);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }

    @Override
    protected List<NonFungibleAsset> mapERC721(Erc721TokenBalances token) {
        return token.getTokens().stream()
                    .map(t -> ethereumOpenSeaGateway.getAsset(token.getAddress(), t.getTokenId().toString()))
                    .map(mapper::map)
                    .collect(Collectors.toList());
    }

    @Override
    protected List<NonFungibleAsset> mapERC1155(Erc1155TokenBalances token) {
        return token.getTokens().stream()
                    .map(t -> ethereumOpenSeaGateway.getAsset(token.getAddress(), t.getTokenId().toString()))
                    .map(mapper::map)
                    .collect(Collectors.toList());
    }
}
