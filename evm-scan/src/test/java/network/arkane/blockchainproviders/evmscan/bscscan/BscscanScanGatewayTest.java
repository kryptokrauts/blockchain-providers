package network.arkane.blockchainproviders.evmscan.bscscan;


import network.arkane.blockchainproviders.evmscan.dto.EvmAccount;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class BscscanScanGatewayTest {

    private BscScanGateway bscScanGateway;

    @BeforeEach
    void setUp() {
        this.bscScanGateway = new BscScanGateway("https://api-testnet.bscscan.com/api/", "YourApiKeyToken");
    }

    @Test
    void getTransactions() {
        final EvmAccount response = bscScanGateway.getTransactionList("0xF426a8d0A94bf039A35CEE66dBf0227A7a12D11e");
        assertThat(response).isNotNull();
        assertThat(response.getChain()).isEqualTo(SecretType.BSC);
        assertThat(response.getAddress()).isEqualTo("0xF426a8d0A94bf039A35CEE66dBf0227A7a12D11e");
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
