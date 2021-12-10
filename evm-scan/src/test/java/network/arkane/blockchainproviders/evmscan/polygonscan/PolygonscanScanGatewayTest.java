package network.arkane.blockchainproviders.evmscan.polygonscan;


import network.arkane.blockchainproviders.evmscan.dto.EvmAccount;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class PolygonscanScanGatewayTest {

    private PolygonscanGateway polygonscanGateway;

    @BeforeEach
    void setUp() {
        this.polygonscanGateway = new PolygonscanGateway("https://api-mumbai.polygonscan.com/api/", "YourApiKeyToken");
    }

    @Test
    void getTransactions() {
        final EvmAccount response = polygonscanGateway.getTransactionList("0x0000000000000000000000000000000000001010");

        assertThat(response).isNotNull();
        assertThat(response.getChain()).isEqualTo(SecretType.MATIC);
        assertThat(response.getAddress()).isEqualTo("0x0000000000000000000000000000000000001010");
        assertThat(response.getTransactions()).isNotEmpty();
        assertThat(response.getTransactions().get(0).getBlockHash()).isNotNull();
        assertThat(response.getTransactions().get(0).getBlockNumber()).isNotNull();
        assertThat(response.getTransactions().get(0).getFrom()).isNotNull();
        assertThat(response.getTransactions().get(0).getHash()).isNotNull();
        assertThat(response.getTransactions().get(0).getIsError()).isNotNull();
        assertThat(response.getTransactions().get(0).getTimestamp()).isNotNull();
        assertThat(response.getTransactions().get(0).getTo()).isNotNull();
        assertThat(response.getTransactions().get(0).getValue()).isNotNull();
    }

}
