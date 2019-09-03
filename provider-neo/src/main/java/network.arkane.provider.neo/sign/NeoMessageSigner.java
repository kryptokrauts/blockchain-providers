package network.arkane.provider.neo.sign;

import io.neow3j.crypto.Sign;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.sign.domain.Signature;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
@Slf4j
public class NeoMessageSigner implements Signer<NeoSignableMessage, NeoSecretKey> {


    @Override
    public Signature createSignature(NeoSignableMessage signable, NeoSecretKey key) {
        try {
            log.debug("Signing raw neo transaction: {}", signable.toString());
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

            Sign.SignatureData signatureData = Sign.signMessage(dataToSign, key.getKey(), signable.isHash());

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
}