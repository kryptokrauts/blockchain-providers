package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class VechainKeystoreExtractionRequest extends ExtractionRequest {
    private String keystore;
    private String password;

    public VechainKeystoreExtractionRequest(final String keystore,
                                            final String password) {
        super(SecretType.VECHAIN);
        this.keystore = keystore;
        this.password = password;
    }
}
