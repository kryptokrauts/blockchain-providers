package network.arkane.provider.avac.nonfungible;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.avac.contract.AvacContractService;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.AzraelNonFungibleStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(name = "avac.nonfungible.strategy", havingValue = "azrael")
public class AvacAzraelNonFungibleStrategy extends AzraelNonFungibleStrategy {

    public AvacAzraelNonFungibleStrategy(AzraelClient avacAzraelClient,
                                         AvacContractService avacContractService,
                                         Optional<CacheManager> cacheManager) {
        super(avacAzraelClient, avacContractService, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.AVAC;
    }
}
