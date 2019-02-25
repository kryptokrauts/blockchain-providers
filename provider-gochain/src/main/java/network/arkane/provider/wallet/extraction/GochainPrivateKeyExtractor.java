package network.arkane.provider.wallet.extraction;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.GochainSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.GochainPrivateKeyExtractionRequest;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;

@Component
@Slf4j
public class GochainPrivateKeyExtractor implements SecretExtractor<GochainPrivateKeyExtractionRequest> {

    @Override
    public SecretKey extract(final GochainPrivateKeyExtractionRequest importWalletRequest) {
        try {
            String sanitizedKey = sanitize(importWalletRequest.getPrivateKey());
            return GochainSecretKey.builder()
                                    .keyPair(ECKeyPair.create(Hex.decodeHex(sanitizedKey))).build();
        } catch (final Exception ex) {
            log.error("Unable to decode Gochain private key {}", importWalletRequest.getPrivateKey());
            throw new IllegalArgumentException("Unable to decode Gochain private key " + importWalletRequest.getPrivateKey());
        }
    }

    private String sanitize(String privateKey) {
        if (privateKey != null && privateKey.startsWith("0x")) {
            return privateKey.substring(2);
        } else {
            return privateKey;
        }
    }

    @Override
    public Class<GochainPrivateKeyExtractionRequest> getImportRequestType() {
        return GochainPrivateKeyExtractionRequest.class;
    }
}
