package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class GochainPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public GochainPrivateKeyExtractionRequest(final String privateKey) {
        super(SecretType.GOCHAIN);
        this.privateKey = privateKey;
    }
}
