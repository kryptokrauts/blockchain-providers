package network.arkane.provider.tron.secret.generation;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.SecretGenerator;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;
import org.tron.common.utils.Utils;

import java.security.SecureRandom;


@Component
public class TronSecretGenerator implements SecretGenerator<TronSecretKey> {

    @Override
    public TronSecretKey generate() {
        try {
            final ECKey ecKeyPair = new ECKey(new SecureRandom());
            return TronSecretKey.builder()
                                .keyPair(ecKeyPair)
                                .build();
        } catch (final Exception ex) {
            throw new RuntimeException("Unable to generate tron keypair");
        }
    }

    @Override
    public SecretType type() {
        return SecretType.TRON;
    }
}
