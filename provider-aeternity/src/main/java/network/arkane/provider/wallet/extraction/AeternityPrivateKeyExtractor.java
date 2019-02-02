package network.arkane.provider.wallet.extraction;

import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.AeternityPrivateKeyExtractionRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AeternityPrivateKeyExtractor implements SecretExtractor<AeternityPrivateKeyExtractionRequest> {

    final KeyPairService keyPairService = new KeyPairServiceFactory().getService();

    @Override
    public SecretKey extract(final AeternityPrivateKeyExtractionRequest importWalletRequest) {
        try {
            return AeternitySecretKey.builder()
                    .keyPair(keyPairService.generateRawKeyPairFromSecret(importWalletRequest.getPrivateKey())).build();
        } catch (final Exception ex) {
            log.error("Unable to decode aeternity private key {}", importWalletRequest.getPrivateKey());
            throw new IllegalArgumentException("Unable to decode aeternity private key " + importWalletRequest.getPrivateKey());
        }
    }

    @Override
    public Class<AeternityPrivateKeyExtractionRequest> getImportRequestType() {
        return AeternityPrivateKeyExtractionRequest.class;
    }
}
