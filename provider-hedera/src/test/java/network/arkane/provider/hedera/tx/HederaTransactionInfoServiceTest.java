package network.arkane.provider.hedera.tx;

import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.tx.TxInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class HederaTransactionInfoServiceTest {

    private HederaTransactionInfoService infoService;

    @BeforeEach
    void setUp() {
        infoService = new HederaTransactionInfoService(HederaTestFixtures.clientFactory());
    }

    @Test
    void getReceipt() {
        TxInfo transaction = infoService.getTransaction("0.0.1543821@1627029847.251607050");
    }
}
