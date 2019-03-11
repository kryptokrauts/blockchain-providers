package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class AeternityPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public AeternityPrivateKeyExtractionRequest(final String privateKey) {
        super(SecretType.AETERNITY);
        this.privateKey = privateKey;
    }
}
