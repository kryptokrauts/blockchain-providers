package network.arkane.provider.hedera.wallet.extraction;

import com.hedera.hashgraph.sdk.PrivateKey;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.hedera.mirror.dto.Accounts;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.wallet.extraction.request.HederaPrivateKeyExtractionRequest;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
@Slf4j
public class HederaPrivateKeyExtractor implements SecretExtractor<HederaPrivateKeyExtractionRequest> {

    private final MirrorNodeClient mirrorNodeClient;

    public HederaPrivateKeyExtractor(MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = mirrorNodeClient;
    }

    @Override
    public SecretKey extract(HederaPrivateKeyExtractionRequest extractionRequest) {
        PrivateKey pk = null;
        Accounts accounts = null;
        try {
            pk = PrivateKey.fromString(extractionRequest.getPrivateKey());
            accounts = mirrorNodeClient.getAccounts(pk.getPublicKey().toString());
        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("A problem occurred while importing your hedera private key")
                    .cause(e)
                    .build();
        }
        accounts.getAccounts().stream()
                .filter(a -> BooleanUtils.isFalse(a.getDeleted()))
                .findFirst()
                .orElseThrow(() -> arkaneException()
                        .errorCode("No account was found for given private key")
                        .build());
        return HederaSecretKey.builder()
                              .key(pk)
                              .build();
    }

    @Override
    public Class<HederaPrivateKeyExtractionRequest> getImportRequestType() {
        return HederaPrivateKeyExtractionRequest.class;
    }
}
