package network.arkane.provider.aeternity.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class AeternityKeystoreExtractionRequest extends ExtractionRequest {
    private String keystore;
    private String password;

    public AeternityKeystoreExtractionRequest() {
        super(SecretType.AETERNITY);
    }

    public AeternityKeystoreExtractionRequest(final String keystore,
                                              final String password) {
        super(SecretType.AETERNITY);
        this.keystore = keystore;
        this.password = password;
    }
}
