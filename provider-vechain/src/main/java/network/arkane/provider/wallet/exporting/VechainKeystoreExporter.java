package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.secret.generation.VechainSecretKey;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;

@Component
public class VechainKeystoreExporter implements KeyExporter<VechainSecretKey> {
    @Override
    public String export(VechainSecretKey key, final String password) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(password, key.getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.vechain")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }
}
