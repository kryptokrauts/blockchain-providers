package network.arkane.provider.tron.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ChainNoLongerSupportedException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TronTransactionSigner extends AbstractTronTransactionSigner<TronTransactionSignable, TronSecretKey> {

    @Override
    public Signature createSignature(final TronTransactionSignable signable,
                                     final TronSecretKey key) {
        throw new ChainNoLongerSupportedException();
    }
}
