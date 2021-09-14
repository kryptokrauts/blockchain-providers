package network.arkane.provider.hedera.balance;

import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.balance.dto.HederaTokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class HederaTokenInfoServiceIntegrationTest {

    private HederaTokenInfoService tokenInfoService;

    @BeforeEach
    void setUp() {
        tokenInfoService = new HederaTokenInfoService(HederaTestFixtures.clientFactory(), HederaTestFixtures.mirrorNodeClient());
    }

    @Test
    void getTokenInfo() {
        HederaTokenInfo tokenInfo = tokenInfoService.getTokenInfo("0.0.2517364");

        System.out.println(tokenInfo);
    }
}
