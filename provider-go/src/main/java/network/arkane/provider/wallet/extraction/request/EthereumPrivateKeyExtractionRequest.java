package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class EthereumPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    public EthereumPrivateKeyExtractionRequest(final String privateKey) {
        super(SecretType.ETHEREUM);
        this.privateKey = privateKey;
    }
}
