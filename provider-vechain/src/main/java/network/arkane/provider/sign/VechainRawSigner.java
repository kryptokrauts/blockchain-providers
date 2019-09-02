package network.arkane.provider.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.sign.domain.HexSignature;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import java.nio.charset.StandardCharsets;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Slf4j
@Component
public class VechainRawSigner implements Signer<VechainRawSignable, VechainSecretKey> {

    private static final String MESSAGE_PREFIX = "\u0019Vechain Signed Message:\n";

    @Override
    public HexSignature createSignature(VechainRawSignable signable, VechainSecretKey key) {
        try {
            log.debug("Signing raw ethereum transaction: {}", signable.toString());
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
            final Sign.SignatureData signatureData = signable.isPrefix() ? signPrefixedMessage(dataToSign, key.getKeyPair())
                                                                         : signable.isHash()
                                                                           ? Sign.signMessage(dataToSign, key.getKeyPair())
                                                                           : Sign.signMessage(dataToSign, key.getKeyPair(), false);
            return HexSignature
                    .builder()
                    .r(signatureData.getR())
                    .s(signatureData.getS())
                    .v(signatureData.getV())
                    .build();
        } catch (final Exception ex) {
            log.error("Unable to sign transaction: {}", ex.getMessage());
            throw arkaneException()
                    .errorCode("transaction.sign.internal-error")
                    .errorCode("A problem occurred trying to sign the raw Ethereum object")
                    .cause(ex)
                    .build();
        }
    }

    private Sign.SignatureData signPrefixedMessage(byte[] message, ECKeyPair keyPair) {
        return Sign.signMessage(getVechainMessageHash(message), keyPair, false);
    }

    private byte[] getVechainMessageHash(byte[] message)  {
        byte[] prefix = getVechainMessagePrefix(message.length);

        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return Hash.sha3(result);

    }

    private byte[] getVechainMessagePrefix(int messageLength) {
        return MESSAGE_PREFIX.concat(String.valueOf(messageLength)).getBytes();
    }
}
