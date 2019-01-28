package network.arkane.provider.bitcoin.sign;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretGenerator;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.bitcoin.unspent.Unspent;
import network.arkane.provider.bitcoin.unspent.UnspentService;
import network.arkane.provider.bitcoin.wallet.generation.BitcoinWalletGenerator;
import network.arkane.provider.bitcoin.wallet.generation.GeneratedBitcoinWallet;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class BitcoinTransactionSignerTest {

    private BitcoinTransactionSigner bitcoinTransactionSigner;
    private BitcoinTransactionFactory transactionFactory;
    private UnspentService unspentService;


    @BeforeEach
    void setUp() {
        final BitcoinEnv bitcoinEnv = new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get());
        unspentService = mock(UnspentService.class);
        transactionFactory = spy(new BitcoinTransactionFactory(bitcoinEnv, unspentService));
        bitcoinTransactionSigner = new BitcoinTransactionSigner(bitcoinEnv, transactionFactory);
    }

    @Test
    void reconstructsKey() {
        final BitcoinWalletGenerator bitcoinWalletGenerator = new BitcoinWalletGenerator(new BitcoinEnv(Network.BTC, TestNet3Params.get()));
        final String pwd = "test";
        final BitcoinSecretKey originalSecretKey = new BitcoinSecretGenerator().generate();
        final GeneratedBitcoinWallet test = bitcoinWalletGenerator.generateWallet(pwd, originalSecretKey);

        final BitcoinSecretKey reconstructedPrivateKey = bitcoinTransactionSigner.reconstructKey(new String(Base64.decodeBase64(JSONUtil.toJson(test.secretAsBase64()))), pwd);
        assertThat(reconstructedPrivateKey.getKey().getPrivateKeyAsHex()).isEqualTo(reconstructedPrivateKey.getKey().getPrivateKeyAsHex());
    }

    @Test
    void signsCorrectly() {
        final BitcoinTransactionSignable transactionSignable = BitcoinTransactionSignable.builder()
                                                                                         .satoshiValue(BigInteger.ONE)
                                                                                         .address("mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H")
                                                                                         .build();
        final BitcoinSecretKey bitcoinSecretKey = BitcoinSecretKey.builder()
                                                                  .key(DumpedPrivateKey.fromBase58(TestNet3Params.get(),
                                                                                                   "92JYtSuKyhrG1fVgtBXUQgT8yNGs6XFFCjz1XLCwg8jFM95GHB6").getKey())
                                                                  .build();

        final List<Unspent> unspents = Arrays.asList(Unspent.builder()
                                                            .amount(1440000)
                                                            .scriptPubKey("76a91464d12bfa319ec47fe040bf6d2dbb3013d85f552588ac")
                                                            .txId("2b6e585f75f4743ab8430dc9dbb2b82a5e5496224f13beb9fa7595d192639649")
                                                            .vOut(1)
                                                            .build());
        when(unspentService.getUnspentForAddress(any(Address.class))).thenReturn(unspents);

        final Signature signature = bitcoinTransactionSigner.createSignature(transactionSignable, bitcoinSecretKey);

        assertThat(signature).isInstanceOf(TransactionSignature.class);
        assertThat(((TransactionSignature) signature).getSignedTransaction()).isEqualTo(
                "010000000149966392d19575fab9be134f2296545e2ab8b2dbc90d43b83a74f4755f586e2b010000008a47304402207e7c8bced87ab8d93d73c57b67f2b342517cba3aca28be959a170c1a58f9081d02206f9b6d952db74f2937be7d33a49cc3e03495350b57d90cccb22f13fd3cecefc881410496e59446aed552e60fb29b6a9c6c71d7c1b38b8396e67d9940dbb867a70e314c591b20a0e59e36259439e1d0b6ae16975ca7097103d8d1ac987a02d121b5cd16ffffffff0201000000000000001976a914152e43ab06896fa3fd029aaeeaac866f7d56182f88acfff81500000000001976a91464d12bfa319ec47fe040bf6d2dbb3013d85f552588ac00000000");
    }

    @Test
    void cantSignWhenTxFactoryThrowsArkaneException() {
        final BitcoinTransactionSignable transactionSignable = BitcoinTransactionSignable.builder()
                                                                                         .satoshiValue(BigInteger.ONE)
                                                                                         .address("mhSwuar1U3Hf6phh2LMkFefkjCgFt8Xg5H")
                                                                                         .build();
        final BitcoinSecretKey bitcoinSecretKey = BitcoinSecretKey.builder()
                                                                  .key(ECKey.fromPrivate(Base58.decode("92JYtSuKyhrG1fVgtBXUQgT8yNGs6XFFCjz1XLCwg8jFM95GHB6")))
                                                                  .build();

        doThrow(new ArkaneException("Test", "test.code")).when(transactionFactory)
                                                         .createBitcoinTransaction(transactionSignable, "myGuVgyoWCc1VgF4DsS6pL9GF3eyF3v7e7");

        assertThatThrownBy(() -> bitcoinTransactionSigner.createSignature(transactionSignable, bitcoinSecretKey)).hasMessageContaining("Test");
    }
}