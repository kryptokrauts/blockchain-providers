package network.arkane.provider.secret.generation;

import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

@Component
public class EthereumSecretGenerator implements SecretGenerator<EthereumSecretKey> {
    @Override
    public EthereumSecretKey generate() {

        try {
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            return EthereumSecretKey.builder()
                                    .keyPair(ecKeyPair)
                                    .build();
        } catch (final Exception ex) {
            throw new RuntimeException("Unable to generate ethereum keypair");
        }
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }
}
