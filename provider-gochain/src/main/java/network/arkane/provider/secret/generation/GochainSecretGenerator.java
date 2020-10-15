package network.arkane.provider.secret.generation;

import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

@Component
public class GochainSecretGenerator extends EvmSecretGenerator {

    @Override
    public SecretType type() {
        return SecretType.GOCHAIN;
    }
}
