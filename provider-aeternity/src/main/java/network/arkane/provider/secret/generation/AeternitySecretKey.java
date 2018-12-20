package network.arkane.provider.secret.generation;

import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

@Data
@NoArgsConstructor
public class AeternitySecretKey implements SecretKey {

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }

    // TODO build with aeternity keypair
}
