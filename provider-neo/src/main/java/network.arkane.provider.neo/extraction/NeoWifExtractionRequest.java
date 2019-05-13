package network.arkane.provider.neo.extraction;

import lombok.Data;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Data
public class NeoWifExtractionRequest extends ExtractionRequest {

    private String wif;

    public NeoWifExtractionRequest() {
        super(SecretType.NEO);
    }

    public NeoWifExtractionRequest(final String wif) {
        this();
        this.wif = wif;
    }

}
