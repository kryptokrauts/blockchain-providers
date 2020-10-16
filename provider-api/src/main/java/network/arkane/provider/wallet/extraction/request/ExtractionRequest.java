package network.arkane.provider.wallet.extraction.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS)
public abstract class ExtractionRequest {

    @Getter
    @JsonIgnore
    private SecretType secretType;

    ExtractionRequest() {
    }

    public ExtractionRequest(SecretType secretType) {
        this.secretType = secretType;
    }
}
