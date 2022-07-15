package network.arkane.provider.nonfungible;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BlockchainProviderNonFungibleGateway implements NonFungibleGateway {

    private static final String ASSETS_URL = "/api/assets";
    private static final String CONTRACTS_URL = "/api/contracts/{contractAddress}";
    private static final String PARAM_WALLET_ADDRESS = "walletAddress";
    private static final String PARAM_CONTRACT_ADDRESSES = "contractAddresses[]";
    private static final String TOKEN_URL = "/api/assets/{contractAddress}/{tokenId}";


    private final SecretType secretType;
    private final BlockchainProviderGatewayClient client;

    public BlockchainProviderNonFungibleGateway(final SecretType secretType,
                                                BlockchainProviderGatewayClient client) {
        this.secretType = secretType;
        this.client = client;
    }

    @Override
    public SecretType getSecretType() {
        return secretType;
    }

    @Override
    public List<NonFungibleAssetBalance> listNonFungibles(final String walletAddress,
                                                          final String... contractAddresses) {
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(ASSETS_URL)
                                                                              .queryParam(PARAM_WALLET_ADDRESS, walletAddress);
        Optional.ofNullable(contractAddresses).ifPresent(addresses -> uriComponentsBuilder.queryParam(PARAM_CONTRACT_ADDRESSES, (Object[]) addresses));
        final String url = uriComponentsBuilder.buildAndExpand().toUriString();
        return Arrays.asList(client.get(url, NonFungibleAssetBalance[].class));
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {
        return client.get(TOKEN_URL, NonFungibleAsset.class, contractAddress, tokenId);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return client.get(CONTRACTS_URL, NonFungibleContract.class, contractAddress);
    }
}
