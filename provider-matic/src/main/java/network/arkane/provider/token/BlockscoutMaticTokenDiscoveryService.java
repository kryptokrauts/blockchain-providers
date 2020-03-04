package network.arkane.provider.token;

import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BlockscoutMaticTokenDiscoveryService implements NativeTokenDiscoveryService {

    private BlockscoutClient maticBlockscoutClient;

    public BlockscoutMaticTokenDiscoveryService(BlockscoutClient maticBlockscoutClient) {
        this.maticBlockscoutClient = maticBlockscoutClient;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        return maticBlockscoutClient.getTokenInfo(tokenAddress);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }


}
