package network.arkane.provider.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.business.token.BusinessNonFungibleGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.MaticContractService;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class MaticNonFungibleGateway extends BlockscoutNonFungibleGateway {

    public MaticNonFungibleGateway(BlockscoutClient maticBlockscoutClient,
                                   MaticContractService maticContractService,
                                   BusinessNonFungibleGateway businessNonFungibleGateway,
                                   Optional<CacheManager> cacheManager) {
        super(maticBlockscoutClient, maticContractService, businessNonFungibleGateway, cacheManager);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }


}
