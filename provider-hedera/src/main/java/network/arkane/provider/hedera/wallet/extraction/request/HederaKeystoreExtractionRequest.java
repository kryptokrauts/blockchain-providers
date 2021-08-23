package network.arkane.provider.hedera.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class HederaKeystoreExtractionRequest extends ExtractionRequest {

    private String keystore;
    private String password;

    HederaKeystoreExtractionRequest() {
    }

    public HederaKeystoreExtractionRequest(
            SecretType secretType,
            final String keystore,
            final String password) {
        super(secretType);
        this.keystore = keystore;
        this.password = password;
    }
}
