package network.arkane.provider.wallet.extraction;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.PrivateKeyExtractionRequest;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;


@Component
@Slf4j
public class VechainPrivateKeyExtractor implements AbstractSecretExtractor<PrivateKeyExtractionRequest> {

    @Override
    public SecretKey extract(final PrivateKeyExtractionRequest importWalletRequest) {
        try {
            String sanitizedKey = sanitize(importWalletRequest.getPrivateKey());
            return VechainSecretKey.builder()
                                   .keyPair(ECKeyPair.create(Hex.decodeHex(sanitizedKey))).build();
        } catch (final Exception ex) {
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

    @Override
    public Class<PrivateKeyExtractionRequest> getImportRequestType() {
        return PrivateKeyExtractionRequest.class;
    }
}
