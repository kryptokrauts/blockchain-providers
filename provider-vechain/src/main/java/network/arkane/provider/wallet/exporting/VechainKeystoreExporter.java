package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.wallet.exporting.request.VechainKeyExportRequest;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;

@Component
public class VechainKeystoreExporter implements KeyExporter<VechainKeyExportRequest> {
    @Override
    public String export(VechainKeyExportRequest extractionRequest) {
        try {

            return JSONUtil.toJson(Wallet.createStandard(extractionRequest.getPassword(), extractionRequest.getSecretKey().getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.vechain")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }
}
