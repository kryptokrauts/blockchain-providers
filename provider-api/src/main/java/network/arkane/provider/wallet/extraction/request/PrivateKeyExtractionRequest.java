package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class PrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public PrivateKeyExtractionRequest(SecretType secretType,
                                       final String privateKey) {
        super(secretType);
        this.privateKey = privateKey;
    }
}
