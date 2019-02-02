package network.arkane.provider.secret.generation;

import com.kryptokrauts.aeternity.sdk.domain.secret.impl.RawKeyPair;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

@Data
@NoArgsConstructor
public class AeternitySecretKey implements SecretKey {

    private RawKeyPair keyPair;

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }

    @Builder
    public AeternitySecretKey(RawKeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
