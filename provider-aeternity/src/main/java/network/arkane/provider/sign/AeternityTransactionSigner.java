package network.arkane.provider.sign;

import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.sign.domain.Signature;

public class AeternityTransactionSigner implements Signer<AeternityTransactionSignable, AeternitySecretKey> {

    @Override
    public Signature createSignature(AeternityTransactionSignable signable, AeternitySecretKey key) {
        // TODO
        return null;
    }

    @Override
    public AeternitySecretKey reconstructKey(String secret, String password) {
        // TODO
        return null;
    }

    @Override
    public Class<AeternityTransactionSignable> getType() {
        return AeternityTransactionSignable.class;
    }
}
