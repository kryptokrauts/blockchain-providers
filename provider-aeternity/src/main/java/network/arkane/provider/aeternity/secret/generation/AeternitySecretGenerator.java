package network.arkane.provider.aeternity.secret.generation;

import com.kryptokrauts.aeternity.sdk.domain.secret.KeyPair;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.SecretGenerator;
import org.springframework.stereotype.Component;

@Component
public class AeternitySecretGenerator implements SecretGenerator<AeternitySecretKey> {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();

    @Override
    public AeternitySecretKey generate() {
        final KeyPair keyPair = keyPairService.generateKeyPair();
        return AeternitySecretKey.builder()
                .keyPair(keyPair)
                .build();
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }
}
