package network.arkane.provider.neo.wallet.extraction;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;


@Getter
public class NeoPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public NeoPrivateKeyExtractionRequest() {
        super(SecretType.NEO);
    }

    public NeoPrivateKeyExtractionRequest(final String privateKey) {
        this();
        this.privateKey = privateKey;
    }
}
