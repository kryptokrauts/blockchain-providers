package network.arkane.provider.token;

import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.chain.SecretType;

import java.util.Optional;

//@Component("blockscoutEthereumTokenDiscoveryService")
//@ConditionalOnBean(name = "ethereumBlockscoutClient")
public class BlockscoutEthereumTokenDiscoveryService implements NativeTokenDiscoveryService {

    private BlockscoutClient ethereumBlockscoutClient;

    public BlockscoutEthereumTokenDiscoveryService(BlockscoutClient ethereumBlockscoutClient) {
        this.ethereumBlockscoutClient = ethereumBlockscoutClient;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        return ethereumBlockscoutClient.getTokenInfo(tokenAddress);
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }


}
