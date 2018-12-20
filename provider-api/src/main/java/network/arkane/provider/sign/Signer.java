package network.arkane.provider.sign;

import network.arkane.provider.sign.domain.Signable;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.wallet.domain.SecretKey;

public abstract class Signer<T extends Signable, KEY extends SecretKey> {

    private Class<T> type;

    public Signer(Class<T> type) {
        this.type = type;
    }

    /**
     * Create a signature, based on a signable and a provided key
     *
     * @param signable
     * @param key
     * @return
     */
    abstract Signature createSignature(T signable, KEY key);

    /**
     * Reconstruct the key, based on the secret and the password
     *
     * @param secret
     * @param password
     * @return
     */
    abstract KEY reconstructKey(final String secret, final String password);

    /**
     * The type of signable this specific signer supports
     *
     * @return
     */
    Class<T> getType() {
        return type;
    }
}
