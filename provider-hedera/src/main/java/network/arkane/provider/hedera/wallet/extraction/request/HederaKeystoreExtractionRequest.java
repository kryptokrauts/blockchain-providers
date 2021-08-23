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
            final String keystore,
            final String password) {
        super(SecretType.HEDERA);
        this.keystore = keystore;
        this.password = password;
    }
}
