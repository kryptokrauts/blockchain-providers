package io.venly.provider.imx.secret;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.web3j.crypto.ECKeyPair;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImxSecretKey implements SecretKey {

    private SecretType type;
    private ECKeyPair keyPair;

    @Override
    public SecretType type() {
        return type;
    }

    @Builder
    public ImxSecretKey(SecretType type,
                        ECKeyPair keyPair) {
        this.type = type;
        this.keyPair = keyPair;
    }
}
