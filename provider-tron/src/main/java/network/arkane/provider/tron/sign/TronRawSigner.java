package network.arkane.provider.tron.sign;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Slf4j
@Component
public class TronRawSigner extends TronTransactionSigner<TronRawSignable, TronSecretKey> {

    @Override
    public Signature createSignature(final TronRawSignable signable,
                                     final TronSecretKey key) {
        try {
            if (signable.getData() == null) {
                throw arkaneException()
                        .errorCode("tron.signature.error")
                        .message("An error occurred trying to create a TRON-signature")
                        .build();
            }

            final byte[] dataToSign;

            if (signable.getData().startsWith("0x")) {
                dataToSign = Hex.decodeHex(signable.getData().replaceFirst("0x", ""));
            } else {
                dataToSign = signable.getData().getBytes(Charsets.UTF_8);
            }

            final byte[] signature = signTransaction2Byte(dataToSign, key.getKeyPair().getPrivKeyBytes());
            return TransactionSignature.signTransactionBuilder()
                                       .signedTransaction(Hex.encodeHexString(signature))
                                       .build();
        } catch (Exception ex) {
            log.error("Unable to sign transaction: {}", ex.getMessage());
            throw arkaneException()
                    .errorCode("transaction.sign.internal-error")
                    .errorCode("A problem occurred trying to sign the raw Tron object")
                    .cause(ex)
                    .build();
        }
    }
}
