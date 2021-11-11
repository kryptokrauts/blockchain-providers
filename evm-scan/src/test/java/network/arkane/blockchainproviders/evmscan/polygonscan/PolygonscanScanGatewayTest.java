package network.arkane.blockchainproviders.evmscan.polygonscan;


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

class PolygonscanScanGatewayTest {

    private PolygonscanClient polygonscanClient;
    private PolygonscanGateway polygonscanGateway;

    @BeforeEach
    void setUp() {
        this.polygonscanClient = mock(PolygonscanClient.class);
        this.polygonscanGateway = new PolygonscanGateway(polygonscanClient);
    }

    @Test
    void getTransactions() {
        final List<EvmTransaction> transactions = Collections.singletonList(EvmTransaction.builder().hash("hash").build());
        final EvmAccount evmAccount = EvmAccount.builder()
                                                .chain(SecretType.MATIC)
                                                .address("walletAddress")
                                                .transactions(transactions)
                                                .build();

        when(polygonscanClient.getTransactionList("walletAddress", 0L, 0L)).thenReturn(EvmScanApiResponse.<EvmTransaction>builder().result(transactions).build());

        final EvmAccount response = polygonscanGateway.getTransactionList("walletAddress");

        assertThat(response).isEqualTo(evmAccount);
    }

}
