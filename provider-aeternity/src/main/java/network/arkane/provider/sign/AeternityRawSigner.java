package network.arkane.provider.sign;

import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.sign.domain.DataSignature;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.wallet.domain.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class AeternityRawSigner implements Signer<AeternityRawSignable, AeternitySecretKey> {

    @Override
    public Signature createSignature(AeternityRawSignable signable, AeternitySecretKey key) {
        return DataSignature.builder().build();
    }
}
