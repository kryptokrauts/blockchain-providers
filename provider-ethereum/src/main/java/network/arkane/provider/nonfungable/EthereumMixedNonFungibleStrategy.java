package network.arkane.provider.nonfungable;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.azrael.dto.token.erc1155.Erc1155TokenBalances;
import network.arkane.blockchainproviders.azrael.dto.token.erc721.Erc721TokenBalances;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.EthereumContractService;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.nonfungible.AzraelNonFungibleStrategy;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.NonFungibleContractTypeMapper;
import network.arkane.provider.opensea.OpenSeaAssetToNonFungibleAssetMapper;
import network.arkane.provider.opensea.OpenSeaContractToNonFungibleContractMapper;
import network.arkane.provider.opensea.OpenSeaGateway;
import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.AssetContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "ethereum.nonfungible.strategy", havingValue = "mixed")
public class EthereumMixedNonFungibleStrategy extends AzraelNonFungibleStrategy {


    private final OpenSeaContractToNonFungibleContractMapper contractMapper;
    private final OpenSeaGateway ethereumOpenSeaGateway;
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
    protected List<Callable<NonFungibleAsset>> mapERC721(Erc721TokenBalances token) {
        return token.getTokens().stream()
                    .map(t -> (Callable<NonFungibleAsset>) () -> {
                        if (StringUtils.isBlank(t.getMetadata())) {
                            Asset asset = ethereumOpenSeaGateway.getAsset(token.getAddress(), t.getTokenId().toString());
                            NonFungibleAsset result = mapper.map(asset);
                            if (result != null && StringUtils.isNotBlank(result.getTokenId())) {
                                return result;
                            }
                        }
                        return super.getNonFungibleAsset(t.getTokenId().toString(), createContract(token), t.getMetadata(), BigInteger.ONE);
                    })
                    .collect(Collectors.toList());
    }

    @Override
    protected List<Callable<NonFungibleAsset>> mapERC1155(Erc1155TokenBalances token) {
        return token.getTokens()
                    .stream()
                    .map(t -> (Callable<NonFungibleAsset>) () -> {
                        if (StringUtils.isBlank(t.getMetadata())) {
                            Asset asset = ethereumOpenSeaGateway.getAsset(token.getAddress(), t.getTokenId().toString());
                            NonFungibleAsset result = mapper.map(asset);
                            if (result != null && StringUtils.isNotBlank(result.getTokenId())) {
                                return result;
                            }
                        }
                        return super.getNonFungibleAsset(t.getTokenId().toString(), createContract(token), t.getMetadata(), BigInteger.ONE);
                    })
                    .collect(Collectors.toList());
    }

    @Override
    public NonFungibleAsset getNonFungible(String contractAddress,
                                           String tokenId) {
        Asset asset = ethereumOpenSeaGateway.getAsset(contractAddress, tokenId);
        return mapper.map(asset);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(String contractAddress) {
        AssetContract asset = ethereumOpenSeaGateway.getContract(contractAddress);
        return contractMapper.map(asset);
    }

    @Override
    protected NonFungibleAsset getNonFungibleAsset(String tokenId,
                                                   NonFungibleContract contract,
                                                   String strMetaData,
                                                   BigInteger balance) {
        return getNonFungible(contract.getAddress(), tokenId);
    }
}
