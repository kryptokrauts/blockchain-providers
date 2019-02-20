package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class EthereumPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public EthereumPrivateKeyExtractionRequest() {
        super(SecretType.ETHEREUM);
    }

    public EthereumPrivateKeyExtractionRequest(final String privateKey) {
        this();
        this.privateKey = privateKey;
    }
}
