package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class GochainPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public GochainPrivateKeyExtractionRequest() {
        super(SecretType.GOCHAIN);
    }

    public GochainPrivateKeyExtractionRequest(final String privateKey) {
        this();
        this.privateKey = privateKey;
    }
}
