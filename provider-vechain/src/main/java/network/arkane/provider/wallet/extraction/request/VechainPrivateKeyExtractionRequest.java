package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class VechainPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public VechainPrivateKeyExtractionRequest() {
        super(SecretType.VECHAIN);
    }

    public VechainPrivateKeyExtractionRequest(final String privateKey) {
        this();
        this.privateKey = privateKey;
    }
}
