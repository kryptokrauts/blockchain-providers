package network.arkane.provider.secret.generation;

import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

@Component
public class VechainSecretGenerator implements SecretGenerator<VechainSecretKey> {
    @Override
    public VechainSecretKey generate() {

        try {
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            return VechainSecretKey.builder()
                                   .keyPair(ecKeyPair)
                                   .build();
        } catch (final Exception ex) {
            throw new RuntimeException("Unable to generate vechain keypair");
        }
    }

    @Override
    public SecretType type() {
        return SecretType.VECHAIN;
    }
}
