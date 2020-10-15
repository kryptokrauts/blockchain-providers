package network.arkane.provider.bsc.secret.generation;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EvmSecretGenerator;
import org.springframework.stereotype.Component;

@Component
public class BscSecretGenerator extends EvmSecretGenerator {

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }
}
