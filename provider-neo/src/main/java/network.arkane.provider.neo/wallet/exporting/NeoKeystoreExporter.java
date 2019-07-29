package network.arkane.provider.neo.wallet.exporting;

import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;
import io.neow3j.wallet.nep6.NEP6Wallet;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.wallet.decryption.NeoWalletDecryptor;
import network.arkane.provider.neo.wallet.generation.GeneratedNeoWallet;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.springframework.stereotype.Component;


@Component
public class NeoKeystoreExporter implements KeyExporter<NeoSecretKey> {

    private NeoWalletDecryptor neoWalletDecryptor;

    public NeoKeystoreExporter(NeoWalletDecryptor neoWalletDecryptor) {
        this.neoWalletDecryptor = neoWalletDecryptor;
    }

    @Override
    public String export(NeoSecretKey key, final String password) {
        try {
            Account account = Account.fromECKeyPair(key.getKey()).build();

            Wallet wallet = new Wallet.Builder()
                    .account(account)
                    .build();

            wallet.encryptAllAccounts(password);
            return JSONUtil.toJson(wallet.toNEP6Wallet());
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
                .walletFile(JSONUtil.fromJson(secret, NEP6Wallet.class))
                .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.NEO;
    }
}

