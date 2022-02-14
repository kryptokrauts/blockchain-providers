package network.arkane.provider.bitcoin.extraction;

import lombok.Builder;
import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.extraction.request.ExtractionRequest;

@Getter
public class BitcoinWifPassphraseExtractionRequest extends ExtractionRequest {
    private String wif;
    private String passphrase;

    public BitcoinWifPassphraseExtractionRequest() {
        super(SecretType.BITCOIN);
    }

    @Builder
    public BitcoinWifPassphraseExtractionRequest(final String wif,
                                                 final String passphrase) {
        this();
        this.wif = wif;
        this.passphrase = passphrase;
    }
}
