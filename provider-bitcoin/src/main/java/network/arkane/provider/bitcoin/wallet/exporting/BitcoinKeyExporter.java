package network.arkane.provider.bitcoin.wallet.exporting;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.bitcoin.bip38.BIP38EncryptionService;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.bitcoin.wallet.generation.BitcoinKeystore;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.EncryptedData;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.wallet.Protos;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BitcoinKeyExporter implements KeyExporter<BitcoinSecretKey> {

    private static final String BIP_38 = "BIP38";

    private final BIP38EncryptionService bip38EncryptionService;

    public BitcoinKeyExporter(BIP38EncryptionService bip38EncryptionService) {
        this.bip38EncryptionService = bip38EncryptionService;
    }

    @Override
    public String export(BitcoinSecretKey extractionRequest, final String password) {
        try {
            return JSONUtil.toJson(ExportedBitcoinKey.builder()
                                                     .type(BIP_38)
                                                     .value(bip38EncryptionService.encrypt(extractionRequest, password))
                                                     .build());
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("export.bitcoin")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }

    public BitcoinSecretKey reconstructKey(final String secret, final String password) {
        BitcoinKeystore ed = JSONUtil.fromJson(secret, BitcoinKeystore.class);
        Protos.ScryptParameters params = Protos.ScryptParameters.newBuilder().setSalt(ByteString.copyFrom(Base64.decodeBase64(ed.getSalt()))).build();
        KeyCrypterScrypt crypter = new KeyCrypterScrypt(params);
        EncryptedData encryptedData = new EncryptedData(Base64.decodeBase64(ed.getInitialisationVector()), Base64.decodeBase64(ed.getEncryptedBytes()));
        ECKey key = ECKey.fromEncrypted(encryptedData, crypter, Base64.decodeBase64(ed.getPubKey()));
        return new BitcoinSecretKey(key.decrypt(crypter.deriveKey(password)));
    }

    @Override
    public SecretType type() {
        return SecretType.BITCOIN;
    }
}
