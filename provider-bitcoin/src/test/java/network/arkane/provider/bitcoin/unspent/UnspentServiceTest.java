package network.arkane.provider.bitcoin.unspent;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import network.arkane.provider.blockcypher.domain.BlockcypherTransactionRef;
import org.bitcoinj.core.Address;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UnspentServiceTest {

    private BlockcypherGateway blockcypherGateway;
    private BitcoinEnv bitcoinEnv;
    private UnspentService unspentService;

    @BeforeEach
    void setUp() {
        blockcypherGateway = mock(BlockcypherGateway.class);
        bitcoinEnv = new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get());
        unspentService = new UnspentService(blockcypherGateway, bitcoinEnv);
    }

    @Test
    void getUnspents() {
        String address = "mhxqpVGP4AiYNsmFwDGqBPaNimNZkqCX8o";
        BlockcypherAddressUnspents expectedTransaction = createExpectedTransaction();
        when(blockcypherGateway.getUnspentTransactions(bitcoinEnv.getNetwork(), address)).thenReturn(expectedTransaction);

        List<Unspent> unspents = unspentService.getUnspentForAddress(Address.fromBase58(bitcoinEnv.getNetworkParameters(),
                                                                                        address));

        assertThat(unspents).hasSize(1);
        assertThat(unspents.get(0).getAmount()).isEqualTo(13753330);
        assertThat(unspents.get(0).getScriptPubKey()).isEqualTo(expectedTransaction.getTransactionRefs().get(0).getScript());
        assertThat(unspents.get(0).getTxId()).isEqualTo(expectedTransaction.getTransactionRefs().get(0).getTransactionHash());
        assertThat(unspents.get(0).getVOut()).isEqualTo(expectedTransaction.getTransactionRefs().get(0).getTransactionOutputN().longValue());

    }

    private BlockcypherAddressUnspents createExpectedTransaction() {
        return new BlockcypherAddressUnspents(
                null, null, null, null, null, null, 0, 0, 0,
                Collections.singletonList(BlockcypherTransactionRef.builder()
                                                                   .script("76a914add10b1fd3827683b0071ce3354590477d69d89688ac")
                                                                   .confirmations(10L)
                                                                   .transactionOutputN(BigInteger.ZERO)
                                                                   .transactionHash(
                                                                           "6c6a27da3f5fa89a099061ad093a48e14d12a4055cb42502ffc61bd10e686bbf")
                                                                   .value(new BigInteger("13753330"))
                                                                   .build())
        );

    }
}