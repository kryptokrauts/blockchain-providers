package io.venly.provider.imx.extraction;

import io.venly.provider.imx.extraction.request.ImxPrivateKeyExtractionRequest;
import io.venly.provider.imx.secret.ImxSecretKey;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;

@Component
@Slf4j
public class ImxPrivateKeyExtractor implements SecretExtractor<ImxPrivateKeyExtractionRequest> {

    @Override
    public SecretKey extract(final ImxPrivateKeyExtractionRequest importWalletRequest) {
        try {
            String sanitizedKey = sanitize(importWalletRequest.getPrivateKey());
            return ImxSecretKey.builder().type(importWalletRequest.getSecretType())
                               .keyPair(ECKeyPair.create(Hex.decodeHex(sanitizedKey))).build();
        } catch (final Exception ex) {
            log.error("Unable to decode ethereum private key {}", importWalletRequest.getPrivateKey());
            throw new IllegalArgumentException("Unable to decode ethereum private key " + importWalletRequest.getPrivateKey());
        }
    }

    private String sanitize(String privateKey) {
        if (privateKey != null && privateKey.startsWith("0x")) {
            return privateKey.substring(2);
        } else {
            return privateKey;
        }
    }
}
