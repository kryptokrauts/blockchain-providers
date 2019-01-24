package network.arkane.provider.bitcoin.wallet.exporting;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.bitcoin.bip38.BIP38EncryptionService;
import network.arkane.provider.bitcoin.wallet.exporting.request.BitcoinKeyExportRequest;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BitcoinKeyExporter implements KeyExporter<BitcoinKeyExportRequest> {

    private static final String BIP_38 = "BIP38";

    private final BIP38EncryptionService bip38EncryptionService;

    public BitcoinKeyExporter(BIP38EncryptionService bip38EncryptionService) {
        this.bip38EncryptionService = bip38EncryptionService;
    }

    @Override
    public String export(BitcoinKeyExportRequest extractionRequest) {
        try {
            return JSONUtil.toJson(ExportedBitcoinKey.builder()
                                                     .type(BIP_38)
                                                     .value(bip38EncryptionService.encrypt(extractionRequest.getSecretKey(), extractionRequest.getPassword()))
                                                     .build());
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("export.bitcoin")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }
}
