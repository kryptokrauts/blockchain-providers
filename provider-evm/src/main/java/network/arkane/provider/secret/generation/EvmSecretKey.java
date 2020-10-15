package network.arkane.provider.secret.generation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.web3j.crypto.ECKeyPair;

@Data
@NoArgsConstructor
public class EvmSecretKey implements SecretKey {

    private SecretType type;
    private ECKeyPair keyPair;

    @Override
    public SecretType type() {
        return type;
    }

    @Builder
    public EvmSecretKey(SecretType type,
                        ECKeyPair keyPair) {
        this.type = type;
        this.keyPair = keyPair;
    }
}
