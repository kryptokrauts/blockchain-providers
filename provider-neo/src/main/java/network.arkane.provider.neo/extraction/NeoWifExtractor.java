package network.arkane.provider.neo.extraction;

import io.neow3j.crypto.WIF;

import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;

import org.springframework.stereotype.Component;
import io.neow3j.crypto.ECKeyPair;

@Component
public class NeoWifExtractor implements SecretExtractor<network.arkane.provider.neo.extraction.NeoWifExtractionRequest> {

    @Override
    public NeoSecretKey extract(network.arkane.provider.neo.extraction.NeoWifExtractionRequest extractionRequest) {
        return NeoSecretKey.builder()
                               .key(ECKeyPair.create(WIF.getPrivateKeyFromWIF(extractionRequest.getWif())))
                               .build();
    }
}
