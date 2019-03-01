package network.arkane.provider.litecoin.wallet.exporting;

import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bip38.LitecoinBIP38;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretGenerator;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.litecoin.wallet.generation.GeneratedLitecoinWallet;
import network.arkane.provider.litecoin.wallet.generation.LitecoinWalletGenerator;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.BIP38PrivateKey;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


// Tests only work in Java 9 or after the installation of Unlimited Strength Jurisdiction Policy Files
@Disabled
class LitecoinKeyExporterTest {

    private final LitecoinEnv litecoinEnv = new LitecoinEnv(Network.LITECOIN, new LitecoinParams());

    private LitecoinKeyExporter litecoinKeyExporter = new LitecoinKeyExporter(
            litecoinEnv,
            new LitecoinBIP38(litecoinEnv)
    );

    @Test
    void type() {
        SecretType type = litecoinKeyExporter.type();

        assertThat(type).isEqualTo(SecretType.LITECOIN);
    }


    @Test
    void exportsKey() throws Exception {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        ECKey keyPair = ECKey.fromPrivate(ecKeyPair.getPrivateKey());

        String result = litecoinKeyExporter.export(new LitecoinSecretKey(keyPair), "some password");

        ECKey decryptedKey = BIP38PrivateKey.fromBase58(new LitecoinParams(), result).decrypt("some password");
        assertThat(decryptedKey).isEqualTo(decryptedKey);
    }


    @Test
    void reconstructsKey() {
        LitecoinWalletGenerator litecoinWalletGenerator = new LitecoinWalletGenerator(new LitecoinEnv(Network.LITECOIN, new LitecoinParams()));
        final GeneratedLitecoinWallet wallet = (GeneratedLitecoinWallet) litecoinWalletGenerator.generateWallet("password", new LitecoinSecretGenerator().generate());


        LitecoinSecretKey result = litecoinKeyExporter.reconstructKey(new String(Base64.decodeBase64(wallet.secretAsBase64())), "password");

        assertThat(result.type()).isEqualTo(SecretType.LITECOIN);
    }

    @Test
    void reconstructsWithBadPassphrase() {
        LitecoinWalletGenerator litecoinWalletGenerator = new LitecoinWalletGenerator(new LitecoinEnv(Network.LITECOIN, new LitecoinParams()));
        final GeneratedLitecoinWallet wallet = (GeneratedLitecoinWallet) litecoinWalletGenerator.generateWallet("password", new LitecoinSecretGenerator().generate());

        assertThatThrownBy(
                () -> litecoinKeyExporter.reconstructKey(new String(Base64.decodeBase64(wallet.secretAsBase64())), "wrong-password")
        ).hasMessage("Unable to create export format from secret key")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.export-error")
                .isInstanceOf(ArkaneException.class);
    }
}