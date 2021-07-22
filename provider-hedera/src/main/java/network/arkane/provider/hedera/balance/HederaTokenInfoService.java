package network.arkane.provider.hedera.balance;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfo;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import network.arkane.provider.exceptions.ArkaneException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
public class HederaTokenInfoService {
    private final Client hederaClient;

    public HederaTokenInfoService(Client hederaClient) {
        this.hederaClient = hederaClient;
    }

    @Cacheable("hedera-token-info")
    public TokenInfo getTokenInfo(String tokenId) {
        try {
            return new TokenInfoQuery()
                    .setTokenId(TokenId.fromString(tokenId))
                    .execute(hederaClient);
        } catch (TimeoutException | PrecheckStatusException e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.tokeninfo.error")
                                 .build();
        }
    }
}
