package network.arkane.provider.wallet.exporting.request;

import lombok.Data;
import network.arkane.provider.wallet.domain.SecretKey;

@Data
public class KeyExportRequest<T extends SecretKey> {

    private T secretKey;
    private String password;

    public KeyExportRequest(final T secretKey,
                            final String password) {
        this.secretKey = secretKey;
        this.password = password;
    }
}
