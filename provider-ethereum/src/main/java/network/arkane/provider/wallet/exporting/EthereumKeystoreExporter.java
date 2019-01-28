package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;

@Component
public class EthereumKeystoreExporter implements KeyExporter<EthereumSecretKey> {

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
}
