package network.arkane.provider.bitcoin.bip38;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.NetworkParameters;
import org.springframework.stereotype.Component;

@Component
public class BIP38EncryptionService {

    private NetworkParameters networkParameters;
    private BIP38 bip38;

    public BIP38EncryptionService(final BitcoinEnv bitcoinEnv,
                                  final BIP38 bip38) {
        this.networkParameters = bitcoinEnv.getNetworkParameters();
        this.bip38 = bip38;
    }

    public String encrypt(final BitcoinSecretKey secretKey, final String passphrase) {
        final String privateKeyAsWiF = secretKey.getKey().getPrivateKeyAsWiF(networkParameters);
        return bip38.encryptNoEC(passphrase, privateKeyAsWiF, secretKey.getKey().isCompressed());
    }
}
