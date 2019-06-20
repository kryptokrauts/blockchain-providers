package network.arkane.provider.neo.wallet.extraction;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;


@Getter
public class NeoKeystoreExtractionRequest extends ExtractionRequest {

    private String keystore;
    private String password;

    public NeoKeystoreExtractionRequest() {
        super(SecretType.NEO);
    }

    public NeoKeystoreExtractionRequest(final String keystore, final String password) {
        this();
        this.keystore = keystore;
        this.password = password;
    }
}
