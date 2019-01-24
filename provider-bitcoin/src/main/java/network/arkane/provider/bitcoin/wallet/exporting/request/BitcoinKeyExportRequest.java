package network.arkane.provider.bitcoin.wallet.exporting.request;

import lombok.Builder;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.wallet.exporting.request.KeyExportRequest;

public class BitcoinKeyExportRequest extends KeyExportRequest<BitcoinSecretKey> {

    @Builder
    public BitcoinKeyExportRequest(final BitcoinSecretKey secretKey, final String password) {
        super(secretKey, password);
    }
}
