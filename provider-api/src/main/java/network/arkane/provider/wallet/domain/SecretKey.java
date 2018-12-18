package network.arkane.provider.wallet.domain;

import network.arkane.provider.chain.SecretType;

public interface SecretKey {

    /**
     * The specific type of this secret key
     * @return
     */
    SecretType type();
}
