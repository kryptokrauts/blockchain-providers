package network.arkane.provider.tron.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.tron.wallet.decryption.TronWalletDecryptor;
import network.arkane.provider.tron.wallet.generation.GeneratedTronWallet;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.springframework.stereotype.Component;
import org.tron.keystore.Wallet;
import org.tron.keystore.WalletFile;

@Component
public class TronKeystoreExporter implements KeyExporter<TronSecretKey> {

    private TronWalletDecryptor ethereumWalletDecryptor;

    public TronKeystoreExporter(TronWalletDecryptor ethereumWalletDecryptor) {
        this.ethereumWalletDecryptor = ethereumWalletDecryptor;
    }

    @Override
    public String export(TronSecretKey key, final String password) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(password, key.getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.ethereum")
                                 .message("An error occurred while trying to export the tron-key")
                                 .build();
        }
    }

    @Override
    public TronSecretKey reconstructKey(String secret, String password) {
        return ethereumWalletDecryptor.generateKey(GeneratedTronWallet.builder()
                                                                      .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                      .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.TRON;
    }
}
