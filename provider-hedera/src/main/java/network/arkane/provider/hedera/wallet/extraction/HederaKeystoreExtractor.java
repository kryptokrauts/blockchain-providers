package network.arkane.provider.hedera.wallet.extraction;

import com.hedera.hashgraph.sdk.HederaKeystore;
import com.hedera.hashgraph.sdk.PrivateKey;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.hedera.mirror.dto.Accounts;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.wallet.extraction.request.HederaKeystoreExtractionRequest;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
@Slf4j
public class HederaKeystoreExtractor implements SecretExtractor<HederaKeystoreExtractionRequest> {

    private final MirrorNodeClient mirrorNodeClient;

    public HederaKeystoreExtractor(MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = mirrorNodeClient;
    }

    @Override
    public SecretKey extract(HederaKeystoreExtractionRequest extractionRequest) {
        HederaKeystore hederaKeystore = new HederaKeystore(extractionRequest.getKeystore(), extractionRequest.getPassword());
        PrivateKey pk = hederaKeystore.getPrivateKey();
        Accounts accounts = null;
        try {
            accounts = mirrorNodeClient.getAccounts(pk.getPublicKey().toString());
        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("A problem occurred while importing your keystore")
                    .cause(e)
                    .build();
        }
        accounts.getAccounts().stream()
                .filter(a -> BooleanUtils.isFalse(a.getDeleted()))
                .findFirst()
                .orElseThrow(() -> arkaneException()
                        .errorCode("No account was found for given keystore")
                        .build());
        return HederaSecretKey.builder()
                              .key(pk)
                              .build();
    }

    @Override
    public Class<HederaKeystoreExtractionRequest> getImportRequestType() {
        return HederaKeystoreExtractionRequest.class;
    }
}
