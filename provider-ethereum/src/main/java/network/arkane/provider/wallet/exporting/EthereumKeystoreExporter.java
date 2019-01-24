package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.wallet.exporting.request.EthereumKeyExportRequest;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Wallet;

@Component
public class EthereumKeystoreExporter implements KeyExporter<EthereumKeyExportRequest> {

    @Override
    public String export(EthereumKeyExportRequest extractionRequest) {
        try {
            return JSONUtil.toJson(Wallet.createStandard(extractionRequest.getPassword(), extractionRequest.getSecretKey().getKeyPair()));
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.ethereum")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }
}
