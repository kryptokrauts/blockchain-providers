package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.wallet.decryption.EvmWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedEvmWallet;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

public abstract class EvmKeystoreExporter implements KeyExporter<EvmSecretKey> {

    private EvmWalletDecryptor evmWalletDecryptor;

    public EvmKeystoreExporter(EvmWalletDecryptor evmWalletDecryptor) {
        this.evmWalletDecryptor = evmWalletDecryptor;
    }

    @Override
    public String export(EvmSecretKey key,
                         final String password) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(password, key.getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export." + type().name().toLowerCase())
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }

    @Override
    public EvmSecretKey reconstructKey(String secret,
                                       String password) {
        return evmWalletDecryptor.generateKey(GeneratedEvmWallet.builder()
                                                                .secretType(type())
                                                                .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                .build(), password);
    }

}
