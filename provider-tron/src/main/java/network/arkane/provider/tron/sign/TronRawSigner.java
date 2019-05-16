package network.arkane.provider.tron.sign;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.spongycastle.util.encoders.Hex.decode;

@Component
@Slf4j
public class TronRawSigner extends TronTransactionSigner<TronRawSignable, TronSecretKey> {

    @Override
    public Signature createSignature(final TronRawSignable signable,
                                     final TronSecretKey key) {

        if (signable.getData() == null) {
            throw arkaneException()
                    .errorCode("tron.signature.error")
                    .message("An error occurred trying to create a TRON-signature")
                    .build();
        }

        final byte[] dataToSign;

        if (signable.getData().startsWith("0x")) {
            dataToSign = decode(signable.getData().replaceFirst("0x", ""));
        } else {
            dataToSign = signable.getData().getBytes(Charsets.UTF_8);
        }

        final byte[] signature = signTransaction2Byte(dataToSign, key.getKeyPair().getPrivKeyBytes());
        return TransactionSignature.signTransactionBuilder()
                                   .signedTransaction(encodeHexString(signature))
                                   .build();
    }
}
