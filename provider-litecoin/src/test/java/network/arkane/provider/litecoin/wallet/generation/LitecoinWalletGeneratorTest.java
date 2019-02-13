package network.arkane.provider.litecoin.wallet.generation;


import com.google.protobuf.ByteString;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretGenerator;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.EncryptedData;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.wallet.Protos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LitecoinWalletGeneratorTest {

    private LitecoinWalletGenerator litecoinWalletGenerator;

    @BeforeEach
    void setUp() {
        litecoinWalletGenerator = new LitecoinWalletGenerator(
                new LitecoinEnv(Network.LITECOIN, new LitecoinParams())
        );
    }

    @Test
    void getsType() {
        Class<LitecoinSecretKey> result = litecoinWalletGenerator.type();

        assertThat(result).isEqualTo(LitecoinSecretKey.class);
    }

    @Test
    void generatesWallet() {
        LitecoinSecretKey litecoinSecretKey = new LitecoinSecretGenerator().generate();

        GeneratedWallet result = litecoinWalletGenerator.generateWallet("some pw", litecoinSecretKey);

        assertThat(result.getAddress()).startsWith("L");
        assertThat(result.secretAsBase64()).isNotNull();

        String secret = result.secretAsBase64();
        LitecoinKeystore ed = JSONUtil.fromJson(new String(Base64.decodeBase64(secret)), LitecoinKeystore.class);
        Protos.ScryptParameters params = Protos.ScryptParameters.newBuilder().setSalt(ByteString.copyFrom(Base64.decodeBase64(ed.getSalt()))).build();
        KeyCrypterScrypt crypter = new KeyCrypterScrypt(params);
        EncryptedData encryptedData = new EncryptedData(Base64.decodeBase64(ed.getInitialisationVector()), Base64.decodeBase64(ed.getEncryptedBytes()));
        ECKey ecKey = ECKey.fromEncrypted(encryptedData, crypter, Base64.decodeBase64(ed.getPubKey()));
        ECKey unencrypted = ecKey.decrypt(crypter.deriveKey("some pw"));

        assertThat(unencrypted.getPrivateKeyAsHex()).isEqualTo(litecoinSecretKey.getKey().getPrivateKeyAsHex());
    }
}