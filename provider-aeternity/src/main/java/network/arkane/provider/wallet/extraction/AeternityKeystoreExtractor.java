package network.arkane.provider.wallet.extraction;

import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.AeternityKeystoreExtractionRequest;
import org.springframework.stereotype.Component;

@Component
public class AeternityKeystoreExtractor implements SecretExtractor<AeternityKeystoreExtractionRequest> {

    @Override
    public SecretKey extract(final AeternityKeystoreExtractionRequest importWalletRequest) {
        // TODO build AeternitySecretKey based on KeystoreExtractionRequest
        return null;
    }

    @Override
    public Class<AeternityKeystoreExtractionRequest> getImportRequestType() {
        return AeternityKeystoreExtractionRequest.class;
    }
}
