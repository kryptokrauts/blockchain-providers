package network.arkane.provider.wallet.extraction.request;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@Getter
public class EthereumKeystoreExtractionRequest extends ExtractionRequest {

    private String keystore;
    private String password;

    public EthereumKeystoreExtractionRequest() {
        super(SecretType.ETHEREUM);
    }

    public EthereumKeystoreExtractionRequest(final String keystore,
                                             final String password) {
        this();
        this.keystore = keystore;
        this.password = password;
    }
}
