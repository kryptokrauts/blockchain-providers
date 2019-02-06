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
import org.bitcoinj.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class LitecoinTransactionFactoryTest {

    UnspentLitecoinService unspentLitecoinService;
    LitecoinTransactionFactory litecoinTransactionFactory;
    LitecoinTransactionSignable signable;
    LitecoinP2SHConverter litecoinP2SHConverter;

    @BeforeEach
    void setUp() {
        unspentLitecoinService = mock(UnspentLitecoinService.class);
        litecoinP2SHConverter = mock(LitecoinP2SHConverter.class);
        litecoinTransactionFactory = new LitecoinTransactionFactory(
                new LitecoinEnv(Network.LITECOIN, new LitecoinParams()),
                unspentLitecoinService,
                litecoinP2SHConverter
        );

        signable = new LitecoinTransactionSignable("to address", BigInteger.valueOf(123000), 12);

        when(litecoinP2SHConverter.convert("to address")).thenReturn("LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx");
        when(litecoinP2SHConverter.convert("from address")).thenReturn("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk");
    }

    private Unspent createUnspent(int amount, String txId) {
        return Unspent.builder()
                .amount(amount)
                .vOut(1)
                .txId(txId)
                .scriptPubKey("76a914339a2beda84b3835de419d42164060377c80b8eb88ac")
                .build();
    }

    private TransactionInput createExpectedTransactionInput(Transaction result, String txId, int amount) throws DecoderException {
        return new TransactionInput(
                new LitecoinParams(),
                result,
                Hex.decodeHex("76a914339a2beda84b3835de419d42164060377c80b8eb88ac"),
                new TransactionOutPoint(new LitecoinParams(), 1, Sha256Hash.wrap(txId)),
                Coin.valueOf(amount)
        );
    }

    private TransactionOutput createExpectedTransactionOutput(Transaction result, int amount, String txId) {
        return new TransactionOutput(
                new LitecoinParams(), result, Coin.valueOf(amount),
                Address.fromBase58(new LitecoinParams(), txId));
    }

    @Test
    void createsTransaction() throws Exception {
        when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                createUnspent(123000, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b")
        ));

        Transaction result = litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");

        assertThat(result).isNotNull();
        assertThat(result.getParams()).isEqualTo(new LitecoinParams());
        assertThat(result.getPurpose()).isEqualTo(Transaction.Purpose.USER_PAYMENT);

        assertThat(result.getOutputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionOutput(result, 123000, "LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx")
        );

        assertThat(result.getInputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionInput(result, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 123000)
        );
    }

    @Test
    void createsTransactionThatConsumesMultipleUnspent() throws Exception {
        when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                createUnspent(100000, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b"),
                createUnspent(50000, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c"),
                createUnspent(75000, "346361635f87439b9930c1e9b7fca0c6a89f13d21225008e7042124ddf7b6a70")
        ));

        Transaction result = litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");


        assertThat(result.getOutputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionOutput(result, 123000, "LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx"),
                createExpectedTransactionOutput(result, 22512, "LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")
        );

        assertThat(result.getInputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionInput(result, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 100000), createExpectedTransactionInput(result, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c", 50000)
        );

    }

    @Test
    void createsTransactionThatConsumesMultipleUnspent_includeFeeInCalculation() throws Exception {
        when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                createUnspent(100000, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b"),
                createUnspent(23000, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c"),
                createUnspent(75000, "346361635f87439b9930c1e9b7fca0c6a89f13d21225008e7042124ddf7b6a70")
        ));

        Transaction result = litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");


        assertThat(result.getOutputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionOutput(result, 123000, "LMsEHVPsjWbWdEy6cBN9CTBUrccKjev7Tx"),
                createExpectedTransactionOutput(result, 68736, "LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")
        );

        assertThat(result.getInputs()).usingRecursiveFieldByFieldElementComparator().containsOnly(
                createExpectedTransactionInput(result, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b", 100000), createExpectedTransactionInput(result, "64e034634b7add36a53a16702d60f93f1f9dc39eca6707f1d144019ad7f2f59c", 23000), createExpectedTransactionInput(result, "346361635f87439b9930c1e9b7fca0c6a89f13d21225008e7042124ddf7b6a70", 75000)
        );

    }

    @Test
    void noUnspentForAddress() {
        assertThatThrownBy(() -> {
            when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList());

            litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");
        }).hasMessage("The account you're trying to use as origin in the transaction doesn't have valid inputs to send")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.transaction-inputs")
                .isInstanceOf(ArkaneException.class);
    }

    @Test
    void notEnoughFunds() {
        assertThatThrownBy(() -> {
            when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                    createUnspent(122999, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b")
            ));
            litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");
        }).hasMessage("Not enough funds to create the transaction")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.not-enough-funds")
                .isInstanceOf(ArkaneException.class);
    }

    @Test
    @Disabled
        // TODO
    void senderAddressIsNotAcceptableVersion() {
        assertThatThrownBy(() -> litecoinTransactionFactory.createLitecoinTransaction(signable, "from address"))
                .hasMessageStartingWith("Version code of address did not match acceptable versions for network")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.address-wrong-network")
                .isInstanceOf(ArkaneException.class);
    }

    @Test
    @Disabled
        // TODO
    void receiverAddressIsNotAcceptableVersion() {
        assertThatThrownBy(() -> {
            signable.setAddress("1DSfKJ8rPEGW1HkvEnNCozwXB4itn2a4Bh");
            litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");
        }).hasMessageStartingWith("Version code of address did not match acceptable versions for network")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.address-wrong-network")
                .isInstanceOf(ArkaneException.class);
    }

    @Test
    void transactionShouldBeVerified() {
        assertThatThrownBy(() -> {
            when(unspentLitecoinService.getUnspentForAddress("LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk")).thenReturn(newArrayList(
                    createUnspent(122999, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b"),
                    createUnspent(122999, "8ecf3c16971a0fb16ac09d9fbdab77464ac7f5a5e752a00eb275eb839f7d652b")
            ));
            litecoinTransactionFactory.createLitecoinTransaction(signable, "from address");
        }).hasMessage("An error occurred trying to create the Litecoin transaction: Duplicated outpoint")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.creation-error")
                .isInstanceOf(ArkaneException.class);
    }

}