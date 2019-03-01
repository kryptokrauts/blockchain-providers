package network.arkane.provider.litecoin.wallet.exporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bip38.LitecoinBIP38;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.litecoin.wallet.generation.LitecoinKeystore;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.EncryptedData;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.wallet.Protos;
import org.springframework.stereotype.Component;

@Component
public class LitecoinKeyExporter implements KeyExporter<LitecoinSecretKey> {

    private final LitecoinEnv litecoinEnv;
    private final LitecoinBIP38 litecoinBip38;
    private ObjectMapper objectMapper;

    public LitecoinKeyExporter(LitecoinEnv litecoinEnv, LitecoinBIP38 litecoinBip38) {
        this.litecoinEnv = litecoinEnv;
        this.litecoinBip38 = litecoinBip38;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String export(LitecoinSecretKey key, String password) {
        String privateKeyAsWiF = key.getKey().getPrivateKeyAsWiF(litecoinEnv.getNetworkParameters());
        return litecoinBip38.encryptNoEC(password, privateKeyAsWiF, false);
    }

    @Override
    public LitecoinSecretKey reconstructKey(String secret, String password) {
        try {
            final LitecoinKeystore keystore = objectMapper.readValue(secret, LitecoinKeystore.class);
            final Protos.ScryptParameters params = Protos.ScryptParameters.newBuilder().setSalt(
                    ByteString.copyFrom(Base64.decodeBase64(keystore.getSalt()))).build();
            final KeyCrypterScrypt scrypt = new KeyCrypterScrypt(params);
            final ECKey key = ECKey.fromEncrypted(
                    new EncryptedData(Base64.decodeBase64(keystore.getInitialisationVector()), Base64.decodeBase64(keystore.getEncryptedBytes())),
                    scrypt,
                    Base64.decodeBase64(keystore.getPubKey()));
            return new LitecoinSecretKey(key.decrypt(scrypt, scrypt.deriveKey(password)));

        } catch (final Exception ex) {
            throw ArkaneException
                    .arkaneException()
                    .message("Unable to create export format from secret key")
                    .errorCode("litecoin.export-error")
                    .build();
        }
    }

    @Override
    public SecretType type() {
        return SecretType.LITECOIN;
    }
}
