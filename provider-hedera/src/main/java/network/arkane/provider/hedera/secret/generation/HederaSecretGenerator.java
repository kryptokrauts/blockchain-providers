package network.arkane.provider.hedera.secret.generation;

import com.hedera.hashgraph.sdk.PrivateKey;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.SecretGenerator;
import org.springframework.stereotype.Component;

@Component
public class HederaSecretGenerator implements SecretGenerator<HederaSecretKey> {

    @Override
    public HederaSecretKey generate() {
        final PrivateKey privateKey = PrivateKey.generate();
        return HederaSecretKey.builder().key(privateKey).build();
    }

    @Override
    public SecretType type() {
        return SecretType.HEDERA;
    }
}
