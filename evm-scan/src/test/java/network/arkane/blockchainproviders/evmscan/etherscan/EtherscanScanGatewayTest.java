package network.arkane.blockchainproviders.evmscan.etherscan;


import network.arkane.blockchainproviders.evmscan.dto.EvmAccount;
import network.arkane.blockchainproviders.evmscan.dto.EvmScanApiResponse;
import network.arkane.blockchainproviders.evmscan.dto.EvmTransaction;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EtherscanScanGatewayTest {

    private EtherscanClient etherscanClient;
    private EtherscanGateway etherscanGateway;

    @BeforeEach
    void setUp() {
        this.etherscanClient = mock(EtherscanClient.class);
        this.etherscanGateway = new EtherscanGateway(etherscanClient);
    }

    @Test
    void getTransactions() {
        final List<EvmTransaction> transactions = Collections.singletonList(EvmTransaction.builder().hash("hash").build());
        final EvmAccount evmAccount = EvmAccount.builder()
                                                .chain(SecretType.ETHEREUM)
                                                .address("walletAddress")
                                                .transactions(transactions)
                                                .build();

        when(etherscanClient.getTransactionList("walletAddress", 0L, 0L)).thenReturn(EvmScanApiResponse.<EvmTransaction>builder().result(transactions).build());

        final EvmAccount response = etherscanGateway.getTransactionList("walletAddress");

        assertThat(response).isEqualTo(evmAccount);
    }

}
