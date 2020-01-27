package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class MaticPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public MaticPrivateKeyExtractionRequest() {
        super(SecretType.MATIC);
    }

    public MaticPrivateKeyExtractionRequest(final String privateKey) {
        this();
        this.privateKey = privateKey;
    }
}
