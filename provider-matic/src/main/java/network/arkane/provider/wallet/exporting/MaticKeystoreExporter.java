package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.secret.generation.MaticSecretKey;
import network.arkane.provider.wallet.decryption.MaticWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedMaticWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class MaticKeystoreExporter implements KeyExporter<MaticSecretKey> {

    private MaticWalletDecryptor maticWalletDecryptor;

    public MaticKeystoreExporter(MaticWalletDecryptor maticWalletDecryptor) {
        this.maticWalletDecryptor = maticWalletDecryptor;
    }

    @Override
    public String export(MaticSecretKey key,
                         final String password) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(password, key.getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.matic")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }

    @Override
    public MaticSecretKey reconstructKey(String secret,
                                         String password) {
        return maticWalletDecryptor.generateKey(GeneratedMaticWallet.builder()
                                                                    .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                    .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }
}
