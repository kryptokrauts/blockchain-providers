package io.venly.provider.imx.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class ImxKeystoreExtractionRequest extends ExtractionRequest {

    private String keystore;
    private String password;

    ImxKeystoreExtractionRequest() {
    }

    public ImxKeystoreExtractionRequest(
            SecretType secretType,
            final String keystore,
            final String password) {
        super(secretType);
        this.keystore = keystore;
        this.password = password;
    }
}
