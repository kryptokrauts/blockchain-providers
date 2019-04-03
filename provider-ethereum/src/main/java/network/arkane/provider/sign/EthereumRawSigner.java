package network.arkane.provider.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.sign.domain.HexSignature;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

import java.nio.charset.StandardCharsets;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Slf4j
@Component
public class EthereumRawSigner implements Signer<EthereumRawSignable, EthereumSecretKey> {

    @Override
    public HexSignature createSignature(EthereumRawSignable signable, EthereumSecretKey key) {
        try {
            byte[] dataToSign;
            if (signable.getData() != null && signable.getData().startsWith("0x")) {
                dataToSign = Hex.decodeHex(signable.getData().replaceFirst("0x", ""));
            } else {
                dataToSign = signable.getData().getBytes(StandardCharsets.UTF_8);
            }
            final Sign.SignatureData signatureData = signable.isPrefix() ? Sign.signPrefixedMessage(dataToSign, key.getKeyPair())
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
}
