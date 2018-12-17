package network.arkane.provider.sign;

import network.arkane.provider.wallet.domain.SecretKey;

public interface Signer<T extends Signable, KEY extends SecretKey> {
    Signature createSignature(T signable, KEY key);

    KEY reconstructKey(final String secret, final String password);

    Class<T> getType();
}
