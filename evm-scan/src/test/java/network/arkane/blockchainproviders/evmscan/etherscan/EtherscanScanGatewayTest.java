package network.arkane.blockchainproviders.evmscan.etherscan;


import network.arkane.blockchainproviders.evmscan.dto.EvmAccount;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class EtherscanScanGatewayTest {

    private EtherscanGateway etherscanGateway;

    @BeforeEach
    void setUp() {
        this.etherscanGateway = new EtherscanGateway("https://api-ropsten.etherscan.io/api/", "YourApiKeyToken");
    }

    @Test
    void getTransactions() {
        final EvmAccount response = etherscanGateway.getTransactionList("0xddbd2b932c763ba5b1b7ae3b362eac3e8d40121a");
        assertThat(response).isNotNull();
        assertThat(response.getChain()).isEqualTo(SecretType.ETHEREUM);
        assertThat(response.getAddress()).isEqualTo("0xddbd2b932c763ba5b1b7ae3b362eac3e8d40121a");
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
