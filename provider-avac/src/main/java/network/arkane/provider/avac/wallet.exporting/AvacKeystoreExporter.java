package network.arkane.provider.avac.wallet.exporting;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.decryption.EvmWalletDecryptor;
import network.arkane.provider.wallet.exporting.EvmKeystoreExporter;
import org.springframework.stereotype.Component;

@Component
public class AvacKeystoreExporter extends EvmKeystoreExporter {

    public AvacKeystoreExporter(EvmWalletDecryptor evmWalletDecryptor) {
        super(evmWalletDecryptor);
    }

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }
}
