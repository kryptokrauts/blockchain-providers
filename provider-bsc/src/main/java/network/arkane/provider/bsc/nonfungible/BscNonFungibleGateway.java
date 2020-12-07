package network.arkane.provider.bsc.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.bsc.contract.BscContractService;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.AzraelNonFungibleGateway;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BscNonFungibleGateway extends AzraelNonFungibleGateway {

    public BscNonFungibleGateway(AzraelClient bscAzraelClient,
                                 BscContractService bscContractService,
                                 Optional<CacheManager> cacheManager) {
        super(bscAzraelClient, bscContractService, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }


}
