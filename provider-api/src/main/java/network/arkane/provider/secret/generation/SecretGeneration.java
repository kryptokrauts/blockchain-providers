package network.arkane.provider.secret.generation;


import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SecretGeneration {

    private Map<SecretType, SecretGenerator> generators;

    public SecretGeneration(Map<SecretType, SecretGenerator> generators) {
        this.generators = generators;
    }

    public SecretKey generate(final SecretType secretType) {
        final SecretGenerator secretGenerator = generators.get(secretType);
        if (secretGenerator == null) {
            throw new IllegalArgumentException("Unable to find generator with type " + secretType);
        }
        return secretGenerator.generate();
    }
}
