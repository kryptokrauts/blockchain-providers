package network.arkane.provider.wallet.exporting.request;

import lombok.Builder;
import network.arkane.provider.secret.generation.EthereumSecretKey;

public class EthereumKeyExportRequest extends KeyExportRequest<EthereumSecretKey> {

    @Builder
    public EthereumKeyExportRequest(final EthereumSecretKey secretKey, final String password) {
        super(secretKey, password);
    }
}
