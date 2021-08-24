package network.arkane.provider.bitcoin.bridge;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionResponse;
import network.arkane.provider.blockcypher.domain.TX;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BitcoinTransactionGatewayTest {

    private BitcoinTransactionGateway bitcoinTransactionGateway;
    private BlockcypherGateway blockcypherGateway;

    @BeforeEach
    void setUp() {
        blockcypherGateway = mock(BlockcypherGateway.class);
        bitcoinTransactionGateway = new BitcoinTransactionGateway(blockcypherGateway, new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get()));
    }

    @Test
    void submitTransaction() {
        String sig
                =
                "01000000016c4f9fd0290b9e4c50b6f84544002fe6aaeac10fb377544dc274c7c3f4921b9b000000008a473044022039ff93a8af80cdc589a3c6dade2c57563cc14dcbfab1b7e95f71b01539c1032d022014aad0562835aa369c2766115708448b0487f9f0d7236342d3e7de0cc434114d81410496e59446aed552e60fb29b6a9c6c71d7c1b38b8396e67d9940dbb867a70e314c591b20a0e59e36259439e1d0b6ae16975ca7097103d8d1ac987a02d121b5cd16ffffffff0201000000000000001976a91464d12bfa319ec47fe040bf6d2dbb3013d85f552588acc8edd900000000001976a91464d12bfa319ec47fe040bf6d2dbb3013d85f552588ac00000000";
        BlockCypherRawTransactionResponse response = new BlockCypherRawTransactionResponse(TX.builder().hash("txId").build());
        when(blockcypherGateway.sendSignedTransaction(Network.BTC_TEST, sig)).thenReturn(response);

        Signature result = bitcoinTransactionGateway.submit(new TransactionSignature(sig), Optional.empty());

        assertThat(result).isExactlyInstanceOf(SubmittedAndSignedTransactionSignature.class);
        assertThat(((SubmittedAndSignedTransactionSignature) result).getTransactionHash()).isEqualTo("txId");
    }
}