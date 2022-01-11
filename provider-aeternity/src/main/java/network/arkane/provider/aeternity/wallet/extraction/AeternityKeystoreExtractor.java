package network.arkane.provider.aeternity.wallet.extraction;

import com.kryptokrauts.aeternity.sdk.domain.secret.KeyPair;
import com.kryptokrauts.aeternity.sdk.exception.AException;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreService;
import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreServiceFactory;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.aeternity.wallet.extraction.request.AeternityKeystoreExtractionRequest;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.springframework.stereotype.Component;

@Component
public class AeternityKeystoreExtractor implements SecretExtractor<AeternityKeystoreExtractionRequest> {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    private final KeystoreService keystoreService = new KeystoreServiceFactory().getService();

    @Override
    public SecretKey extract(final AeternityKeystoreExtractionRequest importKeystoreRequest) {
        try {
            final String encodedPrivateKey = keystoreService.recoverEncodedPrivateKey(importKeystoreRequest.getKeystore(), importKeystoreRequest.getPassword());
            final KeyPair recoveredRawKeypair = keyPairService.recoverKeyPair(encodedPrivateKey);
            return AeternitySecretKey.builder().keyPair(recoveredRawKeypair).build();
        } catch (AException e) {
            String msg = "Not a valid keystore file";
            if (e.getMessage() != null && e.getMessage().contains("wrong password")) {
                msg = "Wrong password provided for given keystore file";
            }
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Class<AeternityKeystoreExtractionRequest> getImportRequestType() {
        return AeternityKeystoreExtractionRequest.class;
    }
}
