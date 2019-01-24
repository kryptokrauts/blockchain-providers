package network.arkane.provider.wallet.exporting.request;

import lombok.Builder;
import network.arkane.provider.secret.generation.VechainSecretKey;

public class VechainKeyExportRequest extends KeyExportRequest<VechainSecretKey> {

    @Builder
    public VechainKeyExportRequest(final VechainSecretKey secretKey,
                                   final String password) {
        super(secretKey, password);
    }
}
