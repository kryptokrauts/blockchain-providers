package network.arkane.provider.litecoin.wallet.exporting;

import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bip38.BIP38;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
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
            new BIP38(litecoinEnv)
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
        LitecoinSecretKey result = litecoinKeyExporter.reconstructKey("6PRPZGZvE2PHwQiBd2vqcfKvVqupgkUC8kwgQx5J5nt4E16Y3qtY2cFTkW", "some password");

        assertThat(result.type()).isEqualTo(SecretType.LITECOIN);
        assertThat(result.getKey()).isEqualTo(ECKey.fromPrivate(
                Hex.decode("91f79fabd053ff7fb2c098d54b76a0701abcd93c523fa04926f4310986867f91")
        ));
    }

    @Test
    void reconstructsWithBadPassphrase() {
        assertThatThrownBy(
                () -> litecoinKeyExporter.reconstructKey("6PRPZGZvE2PHwQiBd2vqcfKvVqupgkUC8kwgQx5J5nt4E16Y3qtY2cFTkW", "incorrect pw")

        ).hasMessage("Bad passphrase")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.incorrect-passphrase")
                .isInstanceOf(ArkaneException.class);
    }
}