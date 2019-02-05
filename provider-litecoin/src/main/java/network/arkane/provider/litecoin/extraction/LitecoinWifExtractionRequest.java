package network.arkane.provider.litecoin.extraction;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class LitecoinWifExtractionRequest extends ExtractionRequest {

    private String wif;

    public LitecoinWifExtractionRequest(String wif) {
        super(SecretType.LITECOIN);
        this.wif = wif;
    }
}
