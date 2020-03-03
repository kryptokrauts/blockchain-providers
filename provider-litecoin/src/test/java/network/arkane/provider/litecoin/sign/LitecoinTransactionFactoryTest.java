package network.arkane.provider.litecoin.sign;

import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.address.LitecoinP2SHConverter;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.unspent.Unspent;
import network.arkane.provider.litecoin.unspent.UnspentLitecoinService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.TransactionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class LitecoinTransactionFactoryTest {

    UnspentLitecoinService unspentLitecoinService;
    LitecoinTransactionFactory litecoinTransactionFactory;
    LitecoinTransactionSignable signable;
    LitecoinP2SHConverter litecoinP2SHConverter;
    LitecoinFeeCalculator litecoinFeeCalculator;

    @BeforeEach
    void setUp() {
        unspentLitecoinService = mock(UnspentLitecoinService.class);
        litecoinP2SHConverter = mock(LitecoinP2SHConverter.class);
        litecoinFeeCalculator = mock(LitecoinFeeCalculator.class);

        litecoinTransactionFactory = new LitecoinTransactionFactory(
                new LitecoinEnv(Network.LITECOIN, new LitecoinParams()),
                unspentLitecoinService,
                litecoinP2SHConverter,
                litecoinFeeCalculator);

        signable = new LitecoinTransactionSignable("to address", BigInteger.valueOf(12300000), 12);

        when(litecoinP2SHConverter.convert("to address")).thenReturn("LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx");
        when(litecoinP2SHConverter.convert("from address")).thenReturn("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk");
        when(litecoinFeeCalculator.calculate(any(Transaction.class), eq(12))).thenReturn(100000L);

    }

    private Unspent createUnspent(int amount,
                                  String txId) {
        return Unspent.builder()
                      .amount(amount)
                      .vOut(1)
                      .txId(txId)
                      .scriptPubKey("76a914339a2beda84b3835de419d42164060377c80b8eb88ac")
                      .build();
    }

    private TransactionInput createExpectedTransactionInput(Transaction result,
                                                            String txId,
                                                            int amount) throws DecoderException {
        return new TransactionInput(
                new LitecoinParams(),
                result,
                Hex.decodeHex("76a914339a2beda84b3835de419d42164060377c80b8eb88ac"),
                new TransactionOutPoint(new LitecoinParams(), 1, Sha256Hash.wrap(txId)),
                Coin.valueOf(amount)
        );
    }

    private TransactionOutput createExpectedTransactionOutput(Transaction result,
                                                              int amount,
                                                              String txId) {
        return new TransactionOutput(
                new LitecoinParams(), result, Coin.valueOf(amount),
                Address.fromBase58(new LitecoinParams(), txId));
    }

    @Test
    void createsTransaction() throws Exception {
        when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                createUnspent(12300000, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b")
                                                                                                                       ));

        Transaction result = litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");

        assertThat(result).isNotNull();
        assertThat(result.getParams()).isEqualTo(new LitecoinParams());
        assertThat(result.getPurpose()).isEqualTo(Transaction.Purpose.USER_PAYMENT);

        assertThat(result.getOutputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionOutput(result, 12300000, "LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx")
                                                                                                  );

        assertThat(result.getInputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionInput(result, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 12300000)
                                                                                                 );
    }

    @Test
    void createsTransactionThatConsumesMultipleUnspent() throws Exception {
        when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                createUnspent(10000000, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b"),
                createUnspent(5000000, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c"),
                createUnspent(7500000, "346361635f87439b9930c1e9b7fca0c6a89f13d21225008e7042124ddf7b6a70")
                                                                                                                       ));

        Transaction result = litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");


        assertThat(result.getOutputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionOutput(result, 12300000, "LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx"),
                createExpectedTransactionOutput(result, 2600000, "LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")
                                                                                                  );

        assertThat(result.getInputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionInput(result, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 10000000),
                createExpectedTransactionInput(result, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c", 5000000)
                                                                                                 );

    }

    @Test
    void createsTransactionThatConsumesMultipleUnspent_includeFeeInCalculation() throws Exception {
        when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                createUnspent(10000000, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b"),
                createUnspent(24000000, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c"),
                createUnspent(7500000, "346361635f87439b9930c1e9b7fca0c6a89f13d21225008e7042124ddf7b6a70")
                                                                                                                       ));

        Transaction result = litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");


        assertThat(result.getOutputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionOutput(result, 12300000, "LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx"),
                createExpectedTransactionOutput(result, 21600000, "LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")
                                                                                                  );

        assertThat(result.getInputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionInput(result, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 10000000),
                createExpectedTransactionInput(result, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c", 24000000)
                                                                                                 );

    }

    @Test
    void noUnspentForAddress() {
        assertThatThrownBy(() -> {
            when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList());

            litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");
        }).isEqualToComparingFieldByField(ArkaneException.arkaneException()
                                                         .message("The account you're trying to use as origin in the transaction doesn't have valid inputs to send")
                                                         .errorCode("litecoin.transaction-inputs")
                                                         .build());
    }

    @Test
    void notEnoughFunds() {
        assertThatThrownBy(() -> {
            when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                    createUnspent(122999, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b")
                                                                                                                           ));
            litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");
        }).isEqualToComparingFieldByField(ArkaneException.arkaneException()
                                                         .message("Not enough funds to create the transaction")
                                                         .errorCode("litecoin.not-enough-funds")
                                                         .build());
    }

    @Test
    void transactionShouldBeVerified() {
        assertThatThrownBy(() -> {
            when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                    createUnspent(12299900, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b"),
                    createUnspent(12299900, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b")
                                                                                                                           ));
            litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");
        }).isEqualToComparingFieldByField(ArkaneException.arkaneException()
                                                         .message("An error occurred trying to create the Litecoin transaction: Duplicated outpoint")
                                                         .errorCode("litecoin.creation-error")
                                                         .build());
    }
}
