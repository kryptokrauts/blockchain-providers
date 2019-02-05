package network.arkane.provider.wallet.extraction;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.VechainPrivateKeyExtractionRequest;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;

@Component
@Slf4j
public class VechainPrivateKeyExtractor implements SecretExtractor<VechainPrivateKeyExtractionRequest> {

    @Override
    public SecretKey extract(final VechainPrivateKeyExtractionRequest importWalletRequest) {
        try {
            String sanitizedKey = sanitize(importWalletRequest.getPrivateKey());
            return VechainSecretKey.builder()
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
