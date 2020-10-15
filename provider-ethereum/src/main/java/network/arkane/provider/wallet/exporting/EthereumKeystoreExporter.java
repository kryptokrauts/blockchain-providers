package network.arkane.provider.wallet.exporting;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.decryption.EvmWalletDecryptor;
import org.springframework.stereotype.Component;

@Component
public class EthereumKeystoreExporter extends EvmKeystoreExporter {

    public EthereumKeystoreExporter(EvmWalletDecryptor evmWalletDecryptor) {
        super(evmWalletDecryptor);
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }
}
