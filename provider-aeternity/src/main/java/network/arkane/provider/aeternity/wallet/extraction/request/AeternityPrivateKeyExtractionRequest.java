package network.arkane.provider.aeternity.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class AeternityPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public AeternityPrivateKeyExtractionRequest() {
        super(SecretType.AETERNITY);
    }

    public AeternityPrivateKeyExtractionRequest(final String privateKey) {
        super(SecretType.AETERNITY);
        this.privateKey = privateKey;
    }
}
