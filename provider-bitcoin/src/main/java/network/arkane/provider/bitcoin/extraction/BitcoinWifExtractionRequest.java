package network.arkane.provider.bitcoin.extraction;

import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class BitcoinWifExtractionRequest extends ExtractionRequest {

    private String wif;

    public BitcoinWifExtractionRequest() {
        super(SecretType.BITCOIN);
    }

    public BitcoinWifExtractionRequest(final String wif) {
        this();
        this.wif = wif;
    }
}
