package network.arkane.provider.tron.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static org.tron.common.crypto.ECKey.recoverFromSignature;
import static org.tron.common.utils.ByteUtil.bytesToBigInteger;

@Slf4j
@Component
public class TronRawSigner extends AbstractTronTransactionSigner<TronRawSignable, TronSecretKey> {

    private static final String MESSAGE_PREFIX = "\u0019TRON Signed Message:\n32";

    @Override
    public HexSignature createSignature(final TronRawSignable signable,
                                        final TronSecretKey key) {
        try {


            log.debug("Signing raw tron transaction: {}", signable.toString());
            byte[] dataToSign;
            if (signable.getData() != null && isHexadecimal(signable.getData())) {
                try {
                    dataToSign = Hex.decodeHex(signable.getData().replaceFirst("0x", ""));
                } catch (DecoderException de) {
                    dataToSign = signable.getData().getBytes(StandardCharsets.UTF_8);
                }
            } else {
                dataToSign = signable.getData() == null ? "".getBytes(StandardCharsets.UTF_8) : signable.getData().getBytes(StandardCharsets.UTF_8);
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
        return signMessage(rawBytes, ecKey);
    }

    private static Sign.SignatureData signMessage(byte[] message, ECKey keyPair) {
        BigInteger publicKey = bytesToBigInteger(keyPair.getPubKey());
        byte[] messageHash = getTronMessageHash(message);

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

    static byte[] getTronMessageHash(byte[] message) {
        byte[] prefix = getTronMessagePrefix();
        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return Hash.sha3(result);
    }

    static byte[] getTronMessagePrefix() {
        return MESSAGE_PREFIX.getBytes();
    }

}
