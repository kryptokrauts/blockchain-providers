package network.arkane.provider.tron.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class TronPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public TronPrivateKeyExtractionRequest(final String privateKey) {
        super(SecretType.ETHEREUM);
        this.privateKey = privateKey;
    }
}
