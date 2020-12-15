package network.arkane.provider.bsc.nonfungible;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.bsc.contract.BscContractService;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.AzraelNonFungibleStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(name = "bsc.nonfungible.strategy", havingValue = "azrael")
public class BscAzraelNonFungibleStrategy extends AzraelNonFungibleStrategy {

    public BscAzraelNonFungibleStrategy(AzraelClient bscAzraelClient,
                                        BscContractService bscContractService,
                                        Optional<CacheManager> cacheManager) {
        super(bscAzraelClient, bscContractService, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.BSC;
    }
}
