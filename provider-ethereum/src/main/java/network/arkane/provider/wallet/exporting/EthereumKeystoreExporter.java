package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.wallet.decryption.EthereumWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedEthereumWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class EthereumKeystoreExporter implements KeyExporter<EthereumSecretKey> {

    private EthereumWalletDecryptor ethereumWalletDecryptor;

    public EthereumKeystoreExporter(EthereumWalletDecryptor ethereumWalletDecryptor) {
        this.ethereumWalletDecryptor = ethereumWalletDecryptor;
    }

    @Override
    public String export(EthereumSecretKey key, final String password) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(password, key.getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.ethereum")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }

    @Override
    public EthereumSecretKey reconstructKey(String secret, String password) {
        return ethereumWalletDecryptor.generateKey(GeneratedEthereumWallet.builder()
                                                                          .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                          .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }
}
