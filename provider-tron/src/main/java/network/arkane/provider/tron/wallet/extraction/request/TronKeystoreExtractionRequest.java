package network.arkane.provider.tron.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class TronKeystoreExtractionRequest extends ExtractionRequest {
    private String keystore;
    private String password;

    public TronKeystoreExtractionRequest() {
        super(SecretType.TRON);
    }

    public TronKeystoreExtractionRequest(final String keystore,
                                         final String password) {
        super(SecretType.TRON);
        this.keystore = keystore;
        this.password = password;
    }
}
