package network.arkane.provider.bsc.wallet.exporting;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.decryption.EvmWalletDecryptor;
import network.arkane.provider.wallet.exporting.EvmKeystoreExporter;
import org.springframework.stereotype.Component;

@Component
public class BscKeystoreExporter extends EvmKeystoreExporter {

    public BscKeystoreExporter(EvmWalletDecryptor evmWalletDecryptor) {
        super(evmWalletDecryptor);
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }
}
