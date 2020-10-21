package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class EvmPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    EvmPrivateKeyExtractionRequest() {
    }

    public EvmPrivateKeyExtractionRequest(SecretType secretType,
                                          final String privateKey) {
        super(secretType);
        this.privateKey = privateKey;
    }
}
