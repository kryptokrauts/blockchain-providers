package network.arkane.provider.avac.secret.generation;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EvmSecretGenerator;
import org.springframework.stereotype.Component;

@Component
public class AvacSecretGenerator extends EvmSecretGenerator {

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }
}
