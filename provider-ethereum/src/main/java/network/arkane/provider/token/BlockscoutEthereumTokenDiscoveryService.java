package network.arkane.provider.token;

import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.chain.SecretType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("blockscoutEthereumTokenDiscoveryService")
@ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotBlank('${blockscout.ethereum.url:}')")
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
