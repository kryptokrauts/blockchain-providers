package network.arkane.provider.wallet.exporting;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.decryption.EvmWalletDecryptor;
import org.springframework.stereotype.Component;

@Component
public class MaticKeystoreExporter extends EvmKeystoreExporter {


    public MaticKeystoreExporter(EvmWalletDecryptor evmWalletDecryptor) {
        super(evmWalletDecryptor);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }
}
