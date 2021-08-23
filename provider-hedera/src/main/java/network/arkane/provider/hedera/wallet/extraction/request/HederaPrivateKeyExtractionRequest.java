package network.arkane.provider.hedera.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class HederaPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public HederaPrivateKeyExtractionRequest() {
        super(SecretType.HEDERA);
    }

    public HederaPrivateKeyExtractionRequest(final String privateKey) {
        super(SecretType.HEDERA);
        this.privateKey = privateKey;
    }
}
