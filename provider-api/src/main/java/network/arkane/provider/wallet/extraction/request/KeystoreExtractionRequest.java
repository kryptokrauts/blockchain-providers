package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class KeystoreExtractionRequest extends ExtractionRequest {
    private String keystore;
    private String password;

    public KeystoreExtractionRequest(final SecretType secretType,
                                     final String keystore,
                                     final String password) {
        super(secretType);
        this.keystore = keystore;
        this.password = password;
    }
}
