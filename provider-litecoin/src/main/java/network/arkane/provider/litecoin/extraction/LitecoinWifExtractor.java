package network.arkane.provider.litecoin.extraction;

import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.bitcoinj.core.DumpedPrivateKey;
import org.springframework.stereotype.Component;

@Component
public class LitecoinWifExtractor implements SecretExtractor<LitecoinWifExtractionRequest> {

    private final LitecoinEnv litecoinEnv;

    public LitecoinWifExtractor(LitecoinEnv litecoinEnv) {
        this.litecoinEnv = litecoinEnv;
    }

    @Override
    public LitecoinSecretKey extract(LitecoinWifExtractionRequest extractionRequest) {
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(
                litecoinEnv.getNetworkParameters(),
                extractionRequest.getWif()
        );

        return new LitecoinSecretKey(dumpedPrivateKey.getKey());
    }

    @Override
    public Class<LitecoinWifExtractionRequest> getImportRequestType() {
        return LitecoinWifExtractionRequest.class;
    }
}
