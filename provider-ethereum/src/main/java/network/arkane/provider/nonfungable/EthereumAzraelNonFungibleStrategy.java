package network.arkane.provider.nonfungable;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.EthereumContractService;
import network.arkane.provider.nonfungible.AzraelNonFungibleStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(name = "ethereum.nonfungible.strategy", havingValue = "azrael")
public class EthereumAzraelNonFungibleStrategy extends AzraelNonFungibleStrategy {

    public EthereumAzraelNonFungibleStrategy(AzraelClient ethereumAzraelClient,
                                             EthereumContractService ethereumContractService,
                                             Optional<CacheManager> cacheManager) {
        super(ethereumAzraelClient, ethereumContractService, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }
}
