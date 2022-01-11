package network.arkane.provider.aeternity.secret.generation;

import com.kryptokrauts.aeternity.sdk.domain.secret.KeyPair;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

@Data
@NoArgsConstructor
public class AeternitySecretKey implements SecretKey {

    private KeyPair keyPair;

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }

    @Builder
    public AeternitySecretKey(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
