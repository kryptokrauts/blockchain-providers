package io.venly.provider.imx.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class ImxPrivateKeyExtractionRequest extends ExtractionRequest {
    private String privateKey;

    ImxPrivateKeyExtractionRequest() {
    }

    public ImxPrivateKeyExtractionRequest(SecretType secretType,
                                          final String privateKey) {
        super(secretType);
        this.privateKey = privateKey;
    }
}
