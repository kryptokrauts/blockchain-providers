package network.arkane.provider.nonfungible;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.MaticContractService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(name = "matic.nonfungible.strategy", havingValue = "azrael")
public class MaticAzraelNonFungibleStrategy extends AzraelNonFungibleStrategy {

    public MaticAzraelNonFungibleStrategy(AzraelClient maticAzraelClient,
                                          MaticContractService maticContractService,
                                          Optional<CacheManager> cacheManager) {
        super(maticAzraelClient, maticContractService, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }
}
