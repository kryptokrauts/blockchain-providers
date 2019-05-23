package network.arkane.provider.tron.sign;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;
import org.tron.common.utils.Sha256Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static org.tron.common.crypto.ECKey.recoverFromSignature;
import static org.tron.common.utils.ByteUtil.bytesToBigInteger;

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

            final Sign.SignatureData signatureData = signBytes(dataToSign, key.getKeyPair());
            return HexSignature.builder()
                               .r(signatureData.getR())
                               .s(signatureData.getS())
                               .v(signatureData.getV())
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

    private Sign.SignatureData signBytes(byte[] rawBytes, final ECKey ecKey) {
        return signMessage(rawBytes, ecKey, true);
    }

    private static Sign.SignatureData signMessage(byte[] message, ECKey keyPair, boolean needToHash) {
        BigInteger publicKey = bytesToBigInteger(keyPair.getPubKey());
        byte[] messageHash;
        if (needToHash) {
            messageHash = Sha256Hash.hash(message);
        } else {
            messageHash = message;
        }

        ECKey.ECDSASignature sig = keyPair.sign(messageHash);
        int recId = -1;

        int headerByte;
        for (headerByte = 0; headerByte < 4; ++headerByte) {
            ECKey k = recoverFromSignature(headerByte, sig, messageHash);
            if (k != null && bytesToBigInteger(k.getPubKey()).equals(publicKey)) {
                recId = headerByte;
                break;
            }
        }

        if (recId == -1) {
            throw new RuntimeException("Could not construct a recoverable key. Are your credentials valid?");
        } else {
            headerByte = recId + 27;
            byte v = (byte) headerByte;
            byte[] r = Numeric.toBytesPadded(sig.r, 32);
            byte[] s = Numeric.toBytesPadded(sig.s, 32);
            return new Sign.SignatureData(v, r, s);
        }
    }

}
