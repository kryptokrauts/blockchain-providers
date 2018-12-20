package network.arkane.provider.wallet.extraction;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.AeternityPrivateKeyExtractionRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AeternityPrivateKeyExtractor implements SecretExtractor<AeternityPrivateKeyExtractionRequest> {

    @Override
    public SecretKey extract(final AeternityPrivateKeyExtractionRequest importWalletRequest) {
        // TODO create AeternitySecretKey based on PrivateKeyExtractionRequest
        return null;
    }

    @Override
    public Class<AeternityPrivateKeyExtractionRequest> getImportRequestType() {
        return AeternityPrivateKeyExtractionRequest.class;
    }
}
