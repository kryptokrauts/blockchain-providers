package network.arkane.provider.hedera.sign;

import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.RawSignature;
import network.arkane.provider.sign.domain.Signature;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

@Component
public class HederaRawSigner implements Signer<HederaRawSignable, HederaSecretKey> {
    @Override
    public Signature createSignature(final HederaRawSignable signable,
                                     final HederaSecretKey key) {
        final byte[] signature = key.getKey().sign(decodeRawData(signable.getData()));
        return RawSignature.builder()
                           .signature(Hex.toHexString(signature))
                           .build();
    }
}
