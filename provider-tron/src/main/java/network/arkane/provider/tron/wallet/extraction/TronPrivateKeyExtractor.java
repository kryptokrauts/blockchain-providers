package network.arkane.provider.tron.wallet.extraction;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.tron.wallet.extraction.request.TronPrivateKeyExtractionRequest;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;

@Component
@Slf4j
public class TronPrivateKeyExtractor implements SecretExtractor<TronPrivateKeyExtractionRequest> {

    @Override
    public SecretKey extract(final TronPrivateKeyExtractionRequest importWalletRequest) {
        try {
            String sanitizedKey = sanitize(importWalletRequest.getPrivateKey());
            return TronSecretKey.builder()
                                .keyPair(ECKey.fromPrivate(Hex.decodeHex(sanitizedKey))).build();
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
