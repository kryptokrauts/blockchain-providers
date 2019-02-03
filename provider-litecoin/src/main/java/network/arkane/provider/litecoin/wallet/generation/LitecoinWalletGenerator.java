package network.arkane.provider.litecoin.wallet.generation;

import com.google.protobuf.ByteString;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.wallet.Protos;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LitecoinWalletGenerator implements WalletGenerator<LitecoinSecretKey> {

    @Override
    public GeneratedWallet generateWallet(String password, LitecoinSecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        byte[] salt = KeyCrypterScrypt.randomSalt();

        ECKey encryptedEcKey = encrypt(secret.getKey(), password, salt);
        return GeneratedLitecoinWallet.builder()
                .address(secret.getKey().toAddress(new LitecoinParams()).toBase58())
                .secret(new LitecoinKeystore(Base64.encodeBase64String(encryptedEcKey.getPubKey()),
                        Base64.encodeBase64String(encryptedEcKey.getEncryptedData().initialisationVector),
                        Base64.encodeBase64String(encryptedEcKey.getEncryptedData().encryptedBytes),
                        Base64.encodeBase64String(salt)))
                .build();
    }

    private ECKey encrypt(ECKey key, String password, byte[] salt) {
        Protos.ScryptParameters params = Protos.ScryptParameters.newBuilder().setSalt(
                ByteString.copyFrom(salt)).build();
        final KeyCrypterScrypt scrypt = new KeyCrypterScrypt(params);
        return key.encrypt(scrypt, scrypt.deriveKey(password));

    }

    @Override
    public Class<LitecoinSecretKey> type() {
        return LitecoinSecretKey.class;
    }
}
