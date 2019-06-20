package network.arkane.provider.neo.wallet.extraction;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import io.neow3j.crypto.ECKeyPair;

@Component
@Slf4j
public class NeoPrivateKeyExtractor implements SecretExtractor<NeoPrivateKeyExtractionRequest> {

    @Override
    public SecretKey extract(final NeoPrivateKeyExtractionRequest importWalletRequest) {
        try {
            String sanitizedKey = sanitize(importWalletRequest.getPrivateKey());
            return NeoSecretKey.builder()
                    .key(ECKeyPair.create(Hex.decodeHex(sanitizedKey))).build();
        } catch (final Exception ex) {
            log.error("Unable to decode Neo private key {}", importWalletRequest.getPrivateKey());
            throw new IllegalArgumentException("Unable to decode Neo private key " + importWalletRequest.getPrivateKey());
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