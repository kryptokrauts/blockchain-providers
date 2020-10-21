package network.arkane.provider.secret.generation;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

public abstract class EvmSecretGenerator implements SecretGenerator<EvmSecretKey> {
    @Override
    public EvmSecretKey generate() {

        try {
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            return EvmSecretKey.builder()
                               .type(type())
                               .keyPair(ecKeyPair)
                               .build();
        } catch (final Exception ex) {
            throw new RuntimeException("Unable to generate " + type().name() + " keypair");
        }
    }

}
