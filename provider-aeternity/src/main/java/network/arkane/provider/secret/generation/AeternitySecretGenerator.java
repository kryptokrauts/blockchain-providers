package network.arkane.provider.secret.generation;

import network.arkane.provider.chain.SecretType;

public class AeternitySecretGenerator implements SecretGenerator<AeternitySecretKey> {

    @Override
    public AeternitySecretKey generate() {
        return null;
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }
}
