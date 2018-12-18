package network.arkane.provider.secret.generation;


import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

public interface SecretGenerator<T extends SecretKey> {

    /**
     * Generate a secret
     * @return
     */
    T generate();

    /**
     * The type this secret generator supports
     * @return
     */
    SecretType type();
}
