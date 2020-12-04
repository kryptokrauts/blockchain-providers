package network.arkane.provider.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.MaticContractService;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class MaticNonFungibleGateway extends AzraelNonFungibleGateway {

    public MaticNonFungibleGateway(AzraelClient maticAzraelClient,
                                   MaticContractService maticContractService,
                                   Optional<CacheManager> cacheManager) {
        super(maticAzraelClient, maticContractService, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }


}
