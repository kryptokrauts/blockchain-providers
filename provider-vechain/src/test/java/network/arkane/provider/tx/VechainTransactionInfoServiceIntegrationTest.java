package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.clients.DeltaBalancesContractClient;
import network.arkane.provider.core.model.blockchain.NodeProvider;
import network.arkane.provider.gateway.VechainGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class VechainTransactionInfoServiceIntegrationTest {

    private VechainTransactionInfoService service;
    private BigInteger hasFinalityConfirmations;

    @BeforeEach
    void setUp() {
        NodeProvider nodeProvider = NodeProvider.getNodeProvider();
        nodeProvider.setProvider("https://vechain-test.arkane.network");
        nodeProvider.setTimeout(10000);
        DeltaBalancesContractClient deltaBalancesContractClient = new DeltaBalancesContractClient("0xd8a100bccb8cb27ad7ef64d24e30949497e486aa");
        VechainGateway vechainGateway = new VechainGateway(deltaBalancesContractClient);
        hasFinalityConfirmations = new BigInteger("20");
        final HashMap<SecretType, BigInteger> chainSpecificConfirmationNumbers = new HashMap<>();
        chainSpecificConfirmationNumbers.put(SecretType.ETHEREUM, hasFinalityConfirmations);
        HasReachedFinalityService hasReachedFinalityService = new HasReachedFinalityService(new TransactionConfigurationProperties(chainSpecificConfirmationNumbers));
        service = new VechainTransactionInfoService(vechainGateway, hasReachedFinalityService);
    }

    @Test
    void getStatusMined() {
        TxInfo transaction = service.getTransaction("0xf2beaba61fa18cf565bd64550af90b5fc1a1f2204551c44a9b65a615882b35a2");

        assertThat(transaction.getHash()).isEqualTo("0xf2beaba61fa18cf565bd64550af90b5fc1a1f2204551c44a9b65a615882b35a2");
        assertThat(transaction.getStatus()).isEqualTo(TxStatus.SUCCEEDED);
        assertThat(transaction.getBlockHash()).isEqualTo("0x004bbc6d2e1daebb3f3c71b8ecc9bb1fbf324194d076c672143b7c5583b9bcfb");
        assertThat(transaction.getBlockNumber()).isEqualTo(new BigInteger("4963437"));
        assertThat(transaction.getConfirmations()).isGreaterThan(BigInteger.ONE);
        assertThat(transaction.getHasReachedFinality()).isEqualTo(hasFinalityConfirmations.compareTo(transaction.getConfirmations()) >= 0);
    }

    @Test
    void getStatusNotExising() {
        TxInfo transaction = service.getTransaction("0x4c5e99474f6e997789e148cbba0ca58f4a903574edab5fb5a2acedc7b8b17d28");

        assertThat(transaction.getHash()).isEqualTo("0x4c5e99474f6e997789e148cbba0ca58f4a903574edab5fb5a2acedc7b8b17d28");
        assertThat(transaction.getStatus()).isEqualTo(TxStatus.UNKNOWN);
        assertThat(transaction.getBlockHash()).isNull();
        assertThat(transaction.getBlockNumber()).isNull();
        assertThat(transaction.getConfirmations()).isEqualTo(BigInteger.ZERO);
    }


}
