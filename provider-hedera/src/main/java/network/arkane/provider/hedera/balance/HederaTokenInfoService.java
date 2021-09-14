package network.arkane.provider.hedera.balance;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfo;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.balance.dto.HederaTokenInfo;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class HederaTokenInfoService {
    private final Client hederaClient;
    private final MirrorNodeClient mirrorNodeClient;

    public HederaTokenInfoService(HederaClientFactory clientFactory,
                                  MirrorNodeClient mirrorNodeClient) {
        this.hederaClient = clientFactory.getClientWithOperator();
        this.mirrorNodeClient = mirrorNodeClient;
    }

    @Cacheable("hedera-token-info")
    public HederaTokenInfo getTokenInfo(String tokenId) {
        return getTokenInfoFromMirrorNode(tokenId).orElseGet(() -> getTokenInfoFromChain(tokenId));
    }

    private HederaTokenInfo getTokenInfoFromChain(String tokenId) {
        try {
            TokenInfo tokenInfo = new TokenInfoQuery()
                    .setTokenId(TokenId.fromString(tokenId))
                    .execute(hederaClient);
            return HederaTokenInfo.builder().name(tokenInfo.name).symbol(tokenInfo.symbol).decimals(tokenInfo.decimals).memo(tokenInfo.tokenMemo).build();
        } catch (TimeoutException | PrecheckStatusException e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.tokeninfo.error")
                                 .build();
        }
    }

    private Optional<HederaTokenInfo> getTokenInfoFromMirrorNode(String tokenId) {
        try {
            return Optional.of(mirrorNodeClient.getTokenInfo(tokenId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
