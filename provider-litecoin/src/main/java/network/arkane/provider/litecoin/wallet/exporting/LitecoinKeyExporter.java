package network.arkane.provider.litecoin.wallet.exporting;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bip38.BIP38;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.BIP38PrivateKey;
import org.springframework.stereotype.Component;

@Component
public class LitecoinKeyExporter implements KeyExporter<LitecoinSecretKey> {

    private final LitecoinEnv litecoinEnv;
    private final BIP38 bip38;

    public LitecoinKeyExporter(LitecoinEnv litecoinEnv, BIP38 bip38) {
        this.litecoinEnv = litecoinEnv;
        this.bip38 = bip38;
    }

    @Override
    public String export(LitecoinSecretKey key, String password) {
        String privateKeyAsWiF = key.getKey().getPrivateKeyAsWiF(litecoinEnv.getNetworkParameters());
        return bip38.encryptNoEC(password, privateKeyAsWiF, false);
    }

    @Override
    public LitecoinSecretKey reconstructKey(String secret, String password) {
        try {
            ECKey key = BIP38PrivateKey.fromBase58(litecoinEnv.getNetworkParameters(), secret)
                    .decrypt(password);

            return new LitecoinSecretKey(key);

        } catch (BIP38PrivateKey.BadPassphraseException e) {
            throw ArkaneException
                    .arkaneException()
                    .message("Bad passphrase")
                    .errorCode("litecoin.incorrect-passphrase")
                    .build();
        }

    }

    @Override
    public SecretType type() {
        return SecretType.LITECOIN;
    }
}
