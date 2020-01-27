package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class MaticKeystoreExtractionRequest extends ExtractionRequest {

    private String keystore;
    private String password;

    public MaticKeystoreExtractionRequest() {
        super(SecretType.MATIC);
    }

    public MaticKeystoreExtractionRequest(final String keystore,
                                          final String password) {
        this();
        this.keystore = keystore;
        this.password = password;
    }
}
