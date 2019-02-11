package network.arkane.provider.bitcoin.sign;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.unspent.Unspent;
import network.arkane.provider.bitcoin.unspent.UnspentService;
import network.arkane.provider.blockcypher.Network;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BitcoinTransactionFactoryTest {

    private LinkedList<String> txIds;
    private UnspentService unspentService;

    private BitcoinTransactionFactory bitcoinTransactionFactory;
    private BitcoinEnv bitcoinEnv;

    @BeforeEach
    void setUp() {
        txIds = new LinkedList<>(Arrays.asList("bef66c2101a8b1559f2188344bad96b0576d185cafdacb0fbfb7738f221e820d",
                                               "cff5c1c324d8615677975f7e1ffc46c8b5b6cac82f5d9c2c8b0c69602b658936",
                                               "2b6e585f75f4743ab8430dc9dbb2b82a5e5496224f13beb9fa7595d192639649",
                                               "9fdecfa80622fbdafcbed4325ed13b3a04c34f4148faf488fb70e5f56185147c2019",
                                               "06d79678e68bddd0269ed211f8fd9010a6258220b2005c1e15e03f2e16f041a2"));
        unspentService = mock(UnspentService.class);
        bitcoinEnv = new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get());
        bitcoinTransactionFactory = new BitcoinTransactionFactory(bitcoinEnv, unspentService);
    }

    @Test
    void createBitcoinTransaction() {
        final BigInteger satoshiToSend = new BigInteger("1440000");
        final int feePerByte = 3;
        final String from = "mpi2SkK5vKipCNE9h1HtRgDg6UM44AuN9S";
        final String to = "mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H";
        final BitcoinTransactionSignable transactionSignable = BitcoinTransactionSignable.builder()
                                                                                         .satoshiValue(satoshiToSend)
                                                                                         .address(to)
                                                                                         .feePerByte(feePerByte)
                                                                                         .build();
        final List<Unspent> unspents = buildUnspents(1439999, 13042186);
        final long unspentsSum = unspents.stream().mapToLong(Unspent::getAmount).sum();
        final long expectedTxFee = (2 * 148 + 2 * 34 + 10) * feePerByte;

        when(unspentService.getUnspentForAddress(any(Address.class))).thenReturn(unspents);

        final Transaction tx = bitcoinTransactionFactory.createBitcoinTransaction(transactionSignable, from);

        assertThat(tx.getOutputs()).hasSize(2)
                                   .extracting((transactionOutput) -> transactionOutput.getValue().value)
                                   .contains(satoshiToSend.longValue());

        assertThat(tx.getFee().getValue()).isEqualTo(expectedTxFee);
        assertThat(tx.getInputSum().getValue()).isEqualTo(unspentsSum);
        assertThat(tx.getOutputs()).extracting((output) -> output.getValue().value)
                                   .containsExactlyInAnyOrder(satoshiToSend.longValue(), (unspentsSum - satoshiToSend.longValue() - expectedTxFee));
        assertThat(tx.getOutputs()).extracting((output) -> output.getAddressFromP2PKHScript(bitcoinEnv.getNetworkParameters()).toBase58())
                                   .containsOnlyOnce(to).containsOnly(to, from);
    }

    @Test
    void createBitcoinTransaction_moreUnpentsThanNecessary() {
        final BigInteger satoshiToSend = new BigInteger("1440000");
        final int feePerByte = 3;
        final BitcoinTransactionSignable transactionSignable = BitcoinTransactionSignable.builder()
                                                                                         .satoshiValue(satoshiToSend)
                                                                                         .address("mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H")
                                                                                         .feePerByte(feePerByte)
                                                                                         .build();
        final List<Unspent> unspents = buildUnspents(1439999, 6521093, 3260546, 3260546);
        final int expectedIns = 1439999 + 6521093;
        final long expectedTxFee = (2 * 148 + 2 * 34 + 10) * feePerByte;

        when(unspentService.getUnspentForAddress(any(Address.class))).thenReturn(unspents);

        final Transaction tx = bitcoinTransactionFactory.createBitcoinTransaction(transactionSignable, "mpi2SkK5vKipCNE9h1HtRgDg6UM44AuN9S");

        assertThat(tx.getFee().getValue()).isEqualTo(expectedTxFee);
        assertThat(tx.getInputSum().getValue()).isEqualTo(expectedIns);
        assertThat(tx.getOutputs()).hasSize(2)
                                   .extracting((output) -> output.getValue().value)
                                   .containsExactlyInAnyOrder(satoshiToSend.longValue(), ((expectedIns) - satoshiToSend.longValue() - expectedTxFee));
    }

    @Test
    void createBitcoinTransaction_withoutUnspents() {
        assertThatThrownBy(() -> bitcoinTransactionFactory.createBitcoinTransaction(BitcoinTransactionSignable.builder()
                                                                                                              .satoshiValue(BigInteger.ONE)
                                                                                                              .address("mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H")
                                                                                                              .feePerByte(3)
                                                                                                              .build(),
                                                                                    "mpi2SkK5vKipCNE9h1HtRgDg6UM44AuN9S"))
                .hasMessageContaining("The account you're trying to use as origin in the transaction doesn't have valid inputs to send");
    }

    @Test
    void createBitcoinTransaction_notEnoughUnspents() {
        when(unspentService.getUnspentForAddress(any(Address.class))).thenReturn(buildUnspents(14482194));

        assertThatThrownBy(() -> bitcoinTransactionFactory.createBitcoinTransaction(BitcoinTransactionSignable.builder()
                                                                                                              .satoshiValue(BigInteger.valueOf(14482195))
                                                                                                              .address("mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H")
                                                                                                              .feePerByte(3)
                                                                                                              .build(),
                                                                                    "mpi2SkK5vKipCNE9h1HtRgDg6UM44AuN9S"))
                .hasMessageContaining("Not enough funds to create the transaction");
    }

    @Test
    void createBitcoinTransaction_noFee() {
        when(unspentService.getUnspentForAddress(any(Address.class))).thenReturn(buildUnspents(14482195));

        assertThatThrownBy(() -> bitcoinTransactionFactory.createBitcoinTransaction(BitcoinTransactionSignable.builder()
                                                                                                              .satoshiValue(BigInteger.valueOf(14482195))
                                                                                                              .address("mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H")
                                                                                                              .feePerByte(3)
                                                                                                              .build(),
                                                                                    "mpi2SkK5vKipCNE9h1HtRgDg6UM44AuN9S"))
                .hasMessageContaining("Not enough funds to create the transaction");
    }

    @Test
    void createBitcoinTransaction_notEnoughFundsForFee() {
        when(unspentService.getUnspentForAddress(any(Address.class))).thenReturn(buildUnspents(14482195));

        assertThatThrownBy(() -> bitcoinTransactionFactory.createBitcoinTransaction(BitcoinTransactionSignable.builder()
                                                                                                              .satoshiValue(BigInteger.valueOf(14482195 - 677))
                                                                                                              .address("mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H")
                                                                                                              .feePerByte(3)
                                                                                                              .build(),
                                                                                    "mpi2SkK5vKipCNE9h1HtRgDg6UM44AuN9S"))
                .hasMessageContaining("Not enough funds to create the transaction");
    }

    @Test
    void wrongNetwork() {
        assertThatThrownBy(() -> bitcoinTransactionFactory.createBitcoinTransaction(BitcoinTransactionSignable.builder()
                                                                                                              .satoshiValue(BigInteger.valueOf(14482195))
                                                                                                              .address("1Aw2XPY8hkZVHyHtSYCFnvUWnHtAudN7zb")
                                                                                                              .feePerByte(3)
                                                                                                              .build(),
                                                                                    "mpi2SkK5vKipCNE9h1HtRgDg6UM44AuN9S"))
                .hasMessageContaining("Version code of address did not match acceptable versions for network");
    }

    private List<Unspent> buildUnspents(final int... amounts) {
        return Arrays.stream(amounts)
                     .mapToObj((amount) -> Unspent.builder()
                                                  .amount(amount)
                                                  .scriptPubKey("76a91464d12bfa319ec47fe040bf6d2dbb3013d85f552588ac")
                                                  .txId(txIds.poll())
                                                  .vOut(1)
                                                  .build())
                     .collect(Collectors.toList());
    }
}
