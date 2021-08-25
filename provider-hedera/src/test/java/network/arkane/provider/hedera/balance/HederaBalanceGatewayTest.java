package network.arkane.provider.hedera.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.HederaProperties;
import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class HederaBalanceGatewayTest {

    private HederaBalanceGateway hederaBalanceGateway;

    @BeforeEach
    void setUp() {
        HederaClientFactory clientFactory = HederaTestFixtures.clientFactory();
        hederaBalanceGateway = new HederaBalanceGateway(clientFactory,
                                                        new HederaTokenInfoService(clientFactory, HederaTestFixtures.mirrorNodeClient()),
                                                        new MirrorNodeClient(HederaProperties.builder()
                                                                                             .mirrorNodeApiEndpoint("https://testnet.mirrornode.hedera.com/api/v1")
                                                                                             .build()));
    }

    @Test
    void getBalance() {
        Balance balance = hederaBalanceGateway.getBalance(HederaTestFixtures.getAccountId().toString());

        System.out.println(balance);

    }
}
