package org.web3j.crypto;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.sign.EthereumEip712Signable;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.HexSignature;
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Slf4j
@Component
public class EthereumEip712Signer implements Signer<EthereumEip712Signable, EthereumSecretKey> {


    @Override
    public HexSignature createSignature(EthereumEip712Signable signable,
                                        EthereumSecretKey key) {

        try {
            StructuredDataEncoder encoder = new ArkaneStructuredDataEncoder(signable.getData().toString());
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
