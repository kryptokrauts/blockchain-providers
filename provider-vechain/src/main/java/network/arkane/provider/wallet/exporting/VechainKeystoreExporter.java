package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.wallet.decryption.VechainWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedVechainWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class VechainKeystoreExporter implements KeyExporter<VechainSecretKey> {

    private VechainWalletDecryptor vechainWalletDecryptor;

    public VechainKeystoreExporter(final VechainWalletDecryptor vechainWalletDecryptor) {
        this.vechainWalletDecryptor = vechainWalletDecryptor;
    }

    @Override
    public String export(VechainSecretKey key, final String password) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(password, key.getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.vechain")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }


    @Override
    public VechainSecretKey reconstructKey(String secret, String password) {
        return vechainWalletDecryptor.generateKey(GeneratedVechainWallet.builder()
                                                                        .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                        .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.VECHAIN;
    }
}
