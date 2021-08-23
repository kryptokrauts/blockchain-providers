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
        infoService = new HederaTransactionInfoService(HederaTestFixtures.mirrorNodeClient());
    }

    @Test
    void getReceipt() {
        TxInfo transaction = infoService.getTransaction("0.0.1543821@1629710771.627821506");

        System.out.println(transaction);
    }
}
