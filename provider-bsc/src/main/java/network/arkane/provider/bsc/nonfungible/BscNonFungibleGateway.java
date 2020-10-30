package network.arkane.provider.bsc.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.bsc.contract.BscContractService;
import network.arkane.provider.business.token.BusinessNonFungibleGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.BlockscoutNonFungibleGateway;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BscNonFungibleGateway extends BlockscoutNonFungibleGateway {

    public BscNonFungibleGateway(BlockscoutClient bscBlockscoutClient,
                                 BscContractService bscContractService,
                                 BusinessNonFungibleGateway businessNonFungibleGateway,
                                 Optional<CacheManager> cacheManager) {
        super(bscBlockscoutClient, bscContractService, businessNonFungibleGateway, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.BSC;
    }


}
