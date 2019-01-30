package network.arkane.provider.sign;

import net.jodah.typetools.TypeResolver;
import network.arkane.provider.sign.domain.Signable;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.wallet.domain.SecretKey;

public interface Signer<T extends Signable, KEY extends SecretKey> {

    /**
     * Create a signature, based on a signable and a provided key
     *
     * @param signable
     * @param key
     * @return
     */
    Signature createSignature(T signable, KEY key);

    /**
     * The type of signable this specific signer supports
     *
     * @return
     */

    default Class<T> getType() {
        return (Class<T>) TypeResolver.resolveRawArguments(Signer.class, getClass())[0];
    }

    default Class<KEY> getKeyType() {
        return (Class<KEY>) TypeResolver.resolveRawArguments(Signer.class, getClass())[1];
    }
}
