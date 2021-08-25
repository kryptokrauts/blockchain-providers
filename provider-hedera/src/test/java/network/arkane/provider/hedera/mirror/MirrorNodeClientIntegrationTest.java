package network.arkane.provider.hedera.mirror;

import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.balance.dto.HederaTokenInfo;
import network.arkane.provider.hedera.mirror.dto.Accounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MirrorNodeClientIntegrationTest {

    private MirrorNodeClient mirrorNodeClient;

    @BeforeEach
    void setUp() {
        mirrorNodeClient = HederaTestFixtures.mirrorNodeClient();
    }

    @Test
    void getAccounts() {
        Accounts accounts = mirrorNodeClient.getAccounts(HederaTestFixtures.clientFactory().getClientWithOperator().getOperatorPublicKey().toString());

        System.out.println(accounts);
    }

    @Test
    void getTokenInfo() {
        HederaTokenInfo tokenInfo = mirrorNodeClient.getTokenInfo("0.0.2268875");

        System.out.println(tokenInfo);
    }
}
