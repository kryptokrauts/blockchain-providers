package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.secret.generation.GochainSecretKey;
import network.arkane.provider.wallet.decryption.GochainWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedGochainWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class GochainKeystoreExporter implements KeyExporter<GochainSecretKey> {

    private GochainWalletDecryptor gochainWalletDecryptor;

    public GochainKeystoreExporter(GochainWalletDecryptor gochainWalletDecryptor) {
        this.gochainWalletDecryptor = gochainWalletDecryptor;
    }

    @Override
    public String export(GochainSecretKey key, final String password) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(password, key.getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.gochain")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }

    @Override
    public GochainSecretKey reconstructKey(String secret, String password) {
        return gochainWalletDecryptor.generateKey(GeneratedGochainWallet.builder()
                                                                          .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                          .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.GOCHAIN;
    }
}
