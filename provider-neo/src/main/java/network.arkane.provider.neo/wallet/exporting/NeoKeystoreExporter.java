package network.arkane.provider.neo.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.wallet.decryption.NeoWalletDecryptor;
import network.arkane.provider.neo.wallet.generation.GeneratedNeoWallet;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.springframework.stereotype.Component;
import io.neow3j.crypto.Wallet;
import io.neow3j.crypto.WalletFile;


@Component
public class NeoKeystoreExporter implements KeyExporter<NeoSecretKey> {

    private NeoWalletDecryptor neoWalletDecryptor;

    public NeoKeystoreExporter(NeoWalletDecryptor neoWalletDecryptor) {
        this.neoWalletDecryptor = neoWalletDecryptor;
    }

    @Override
    public String export(NeoSecretKey key, final String password) {
        try {
            final WalletFile walletFile = Wallet.createStandardWallet();
            walletFile.addAccount(Wallet.createStandardAccount(password, key.getKey()));
            return JSONUtil.toJson(walletFile);
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                    .errorCode("export.neo")
                    .message("An error occurred while trying to export the key")
                    .build();
        }
    }

    @Override
    public NeoSecretKey reconstructKey(String secret, String password) {
        return neoWalletDecryptor.generateKey(GeneratedNeoWallet.builder()
                .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }
}

