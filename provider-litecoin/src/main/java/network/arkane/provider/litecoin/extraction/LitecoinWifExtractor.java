package network.arkane.provider.litecoin.extraction;

import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.bitcoinj.core.DumpedPrivateKey;
import org.springframework.stereotype.Component;

@Component
public class LitecoinWifExtractor implements SecretExtractor<LitecoinWifExtractionRequest> {

    @Override
    public LitecoinSecretKey extract(LitecoinWifExtractionRequest extractionRequest) {
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(new LitecoinParams(), extractionRequest.getWif());

        return new LitecoinSecretKey(dumpedPrivateKey.getKey());
    }

    @Override
    public Class<LitecoinWifExtractionRequest> getImportRequestType() {
        return LitecoinWifExtractionRequest.class;
    }
}
