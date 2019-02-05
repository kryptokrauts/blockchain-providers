package network.arkane.provider.secret.generation;

import com.kryptokrauts.aeternity.sdk.domain.secret.impl.RawKeyPair;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import network.arkane.provider.chain.SecretType;

public class AeternitySecretGenerator implements SecretGenerator<AeternitySecretKey> {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();

    @Override
    public AeternitySecretKey generate() {
        final RawKeyPair rawKeyPair = keyPairService.generateRawKeyPair();
        return AeternitySecretKey.builder()
                .keyPair(rawKeyPair)
                .build();
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }
}
