package network.arkane.provider.sign.eip712;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.sign.EvmEip712Signable;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.HexSignature;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Slf4j
@Component
public class EvmEip712Signer implements Signer<EvmEip712Signable, EvmSecretKey> {


    @Override
    public HexSignature createSignature(EvmEip712Signable signable,
                                        EvmSecretKey key) {

        try {
            StructuredDataEncoder encoder = new StructuredDataEncoder(signable.getData());
            byte[] message = encoder.hashStructuredData();
            Sign.SignatureData signatureData = Sign.signMessage(message, key.getKeyPair(), false);
            return HexSignature.builder()
                               .r(signatureData.getR())
                               .s(signatureData.getS())
                               .v(signatureData.getV())
                               .build();
        } catch (Exception e) {
            log.error("Unable to sign transaction: {}", e.getMessage());
            throw arkaneException()
                    .errorCode("transaction.sign.internal-error")
                    .cause(e)
                    .build();
        }

    }
}
