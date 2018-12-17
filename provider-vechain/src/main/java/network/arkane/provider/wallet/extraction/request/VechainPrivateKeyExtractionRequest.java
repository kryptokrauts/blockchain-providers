package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class VechainPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public VechainPrivateKeyExtractionRequest(final String privateKey) {
        super(SecretType.VECHAIN);
        this.privateKey = privateKey;
    }
}
