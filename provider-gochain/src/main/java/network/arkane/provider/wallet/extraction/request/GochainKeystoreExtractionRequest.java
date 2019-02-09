package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class GochainKeystoreExtractionRequest extends ExtractionRequest {
    private String keystore;
    private String password;

    public GochainKeystoreExtractionRequest(final String keystore,
                                             final String password) {
        super(SecretType.GOCHAIN);
        this.keystore = keystore;
        this.password = password;
    }
}
