package network.arkane.provider.litecoin.unspent;

import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import network.arkane.provider.blockcypher.domain.BlockcypherTransactionRef;
import network.arkane.provider.litecoin.address.LitecoinP2SHConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UnspentLitecoinServiceTest {

    UnspentLitecoinService unspentLitecoinService;
    BlockcypherGateway blockcypherGateway;
    LitecoinP2SHConverter litecoinP2SHConverter;

    String address = "VuXFIzGlKwF6A1m5vUI44S9MXBmE7sCDwE";
    String convertedAddress = "3PbLsCPghuXCSomZba4r3eEYbywQgjT9NV";

    @BeforeEach
    void setUp() {
        blockcypherGateway = mock(BlockcypherGateway.class);
        litecoinP2SHConverter = mock(LitecoinP2SHConverter.class);
        unspentLitecoinService = new UnspentLitecoinService(blockcypherGateway, litecoinP2SHConverter);

        when(litecoinP2SHConverter.convert(address)).thenReturn(convertedAddress);
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

    @Test
    void getsUnspentForAddress() {
        BlockcypherAddressUnspents expectedTransaction = createExpectedTransaction();
        when(blockcypherGateway.getUnspentTransactions(Network.LITECOIN, convertedAddress))
                .thenReturn(expectedTransaction);

        List<Unspent> unspents = unspentLitecoinService.getUnspentForAddress(address);

        assertThat(unspents).hasSize(1);
        assertThat(unspents.get(0).getAmount()).isEqualTo(13753330);
        assertThat(unspents.get(0).getScriptPubKey()).isEqualTo(expectedTransaction.getTransactionRefs().get(0).getScript());
        assertThat(unspents.get(0).getTxId()).isEqualTo(expectedTransaction.getTransactionRefs().get(0).getTransactionHash());
        assertThat(unspents.get(0).getVOut()).isEqualTo(expectedTransaction.getTransactionRefs().get(0).getTransactionOutputN().longValue());
    }

    @Test
    void noUnspentWhenGatwayRepliesWithNull() {
        List<Unspent> unspents = unspentLitecoinService.getUnspentForAddress(address);

        assertThat(unspents).isEmpty();
    }

    @Test
    void noUnspentWhenTransactionRefsAreNull() {
        BlockcypherAddressUnspents expectedTransaction = createExpectedTransaction();
        expectedTransaction.setTransactionRefs(null);
        when(blockcypherGateway.getUnspentTransactions(Network.LITECOIN, convertedAddress))
                .thenReturn(expectedTransaction);

        List<Unspent> unspents = unspentLitecoinService.getUnspentForAddress(address);

        assertThat(unspents).isEmpty();
    }

    @Test
    void noUnspentWhenTransactionRefsAreEmpty() {
        BlockcypherAddressUnspents expectedTransaction = createExpectedTransaction();
        expectedTransaction.setTransactionRefs(new ArrayList<>());
        when(blockcypherGateway.getUnspentTransactions(Network.LITECOIN, convertedAddress))
                .thenReturn(expectedTransaction);

        List<Unspent> unspents = unspentLitecoinService.getUnspentForAddress(address);

        assertThat(unspents).isEmpty();
    }

}