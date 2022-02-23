package network.arkane.provider.hedera.nonfungible;

import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.balance.HederaTokenInfoService;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.List;
import java.util.Optional;

@Disabled
class HederaNonfungibleGatewayIntegrationTest {

    private HederaNonfungibleGateway hederaNonfungibleGateway;

    @BeforeEach
    void setUp() {
        MirrorNodeClient mirrorNodeClient = HederaTestFixtures.mirrorNodeClient();
        HederaTokenInfoService tokenInfoService = new HederaTokenInfoService(HederaTestFixtures.clientFactory(), mirrorNodeClient);
        hederaNonfungibleGateway = new HederaNonfungibleGateway(HederaTestFixtures.clientFactory(),
                                                                tokenInfoService,
                                                                mirrorNodeClient,
                                                                Optional.of(new ConcurrentMapCacheManager()),
                                                                new HederaNftContractInfoService(tokenInfoService));
    }

    @Test
    void getNonFungiblesForAddress() {
        List<NonFungibleAssetBalance> nonFungibleAssetBalances = hederaNonfungibleGateway.listNonFungibles("0.0.1543821");
        hederaNonfungibleGateway.listNonFungibles("0.0.1543821");

        System.out.println(nonFungibleAssetBalances);
    }

}
