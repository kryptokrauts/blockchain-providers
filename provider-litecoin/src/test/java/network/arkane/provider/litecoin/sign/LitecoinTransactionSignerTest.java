package network.arkane.provider.litecoin.sign;

import com.subgraph.orchid.encoders.Hex;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.apache.commons.codec.DecoderException;
import org.bitcoinj.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class LitecoinTransactionSignerTest {

    LitecoinTransactionFactory litecoinTransactionFactory;
    LitecoinTransactionSigner signer;
    LitecoinTransactionSignable signable;
    LitecoinSecretKey litecoinSecretKey;

    @BeforeEach
    void setUp() {
        litecoinTransactionFactory = mock(LitecoinTransactionFactory.class);
        signer = new LitecoinTransactionSigner(new LitecoinEnv(Network.LITECOIN, new LitecoinParams()), litecoinTransactionFactory);

        signable = new LitecoinTransactionSignable("LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx", BigInteger.valueOf(123000), 12);
        litecoinSecretKey = new LitecoinSecretKey(ECKey.fromPrivate(Hex.decode("9d2e1932224ce4e814167da2cebd59882b6d1cc05c6eacf499bf7101e7abd5e9")));
    }

    private TransactionInput createTransactionInput(Transaction result, String txId, int amount) throws DecoderException {
        return new TransactionInput(
                new LitecoinParams(),
                result,
                org.apache.commons.codec.binary.Hex.decodeHex("76a914339a2beda84b3835de419d42164060377c80b8eb88ac"),
                new TransactionOutPoint(new LitecoinParams(), 1, Sha256Hash.wrap(txId)),
                Coin.valueOf(amount)
        );
    }

    @Test
    void signsCorrectly() throws Exception {
        Transaction transaction = new Transaction(new LitecoinParams());
        transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);
        transaction.addOutput(Coin.valueOf(1234), Address.fromBase58(new LitecoinParams(), "LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx"));
        transaction.addInput(createTransactionInput(transaction, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 23423));
        transaction.addInput(createTransactionInput(transaction, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 1234));

        when(litecoinTransactionFactory.createLitecoinTransaction(signable, "LYMkyAv4bvYCSu3k2jTfe22wXYv4kvTTcP"))
                .thenReturn(transaction);

        TransactionSignature result = (TransactionSignature) signer.createSignature(signable, litecoinSecretKey);

        assertThat(result.getSignedTransaction())
                .isEqualTo("01000000022b657d9f83eb75b20ea052e7a5f5c74a4677abbd9f9dc06ab10f1a97163ccf8e010000006b483045022100b94ffeb486336245004ad6e2ec429acf7de26681d9900d001c084b5a149c088c02201cc6a24d2ff8036fc69190f73ce021255fe43db873b74d4d095ea093b01ad2cd8121021d9299de22292257903eb27d4d2a566d0cfccbeec2e91d91a51636e64896996cffffffff2b657d9f83eb75b20ea052e7a5f5c74a4677abbd9f9dc06ab10f1a97163ccf8e010000006b483045022100b94ffeb486336245004ad6e2ec429acf7de26681d9900d001c084b5a149c088c02201cc6a24d2ff8036fc69190f73ce021255fe43db873b74d4d095ea093b01ad2cd8121021d9299de22292257903eb27d4d2a566d0cfccbeec2e91d91a51636e64896996cffffffff01d2040000000000001976a9141cfd04442f72b613c9d2f6ac89d340441dcf955b88ac00000000");
    }

    @Test
    void propagatesArkaneException() {
        assertThatThrownBy(() -> {
            when(litecoinTransactionFactory.createLitecoinTransaction(signable, "LYMkyAv4bvYCSu3k2jTfe22wXYv4kvTTcP"))
                    .thenThrow(new ArkaneException("An error", "error-code"));

            signer.createSignature(signable, litecoinSecretKey);
        }).isInstanceOf(ArkaneException.class)
                .hasFieldOrPropertyWithValue("errorCode", "error-code")
                .hasMessage("An error");
    }

    @Test
    void handlesUnknownException() {
        assertThatThrownBy(() -> {
            when(litecoinTransactionFactory.createLitecoinTransaction(signable, "LYMkyAv4bvYCSu3k2jTfe22wXYv4kvTTcP"))
                    .thenThrow(new RuntimeException("An error"));

            signer.createSignature(signable, litecoinSecretKey);
        }).isInstanceOf(ArkaneException.class)
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.signing-error")
                .hasMessage("An error occurred trying to sign the litecoin transaction: An error");
    }
}