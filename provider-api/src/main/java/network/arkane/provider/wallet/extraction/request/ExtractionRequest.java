package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

public abstract class ExtractionRequest {

    @Getter
    private SecretType secretType;

    public ExtractionRequest(SecretType secretType) {
        this.secretType = secretType;
    }
}
