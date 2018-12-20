package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class AeternityKeystoreExtractionRequest extends ExtractionRequest {
    private String keystore;
    private String password;

    public AeternityKeystoreExtractionRequest(final String keystore,
                                              final String password) {
        super(SecretType.AETERNITY);
        this.keystore = keystore;
        this.password = password;
    }
}
