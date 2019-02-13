package network.arkane.provider.secret.generation;

import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

@Component
public class GochainSecretGenerator implements SecretGenerator<GochainSecretKey> {
    @Override
    public GochainSecretKey generate() {

        try {
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            return GochainSecretKey.builder()
                                    .keyPair(ecKeyPair)
                                    .build();
        } catch (final Exception ex) {
            throw new RuntimeException("Unable to generate Gochain keypair");
        }
    }

    @Override
    public SecretType type() {
        return SecretType.GOCHAIN;
    }
}
