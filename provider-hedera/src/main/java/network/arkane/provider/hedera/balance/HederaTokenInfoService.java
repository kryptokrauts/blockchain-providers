package network.arkane.provider.hedera.balance;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfo;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.balance.dto.HederaTokenInfo;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.ipfs.IpfsUtil;
import network.arkane.provider.token.NativeTokenDiscoveryService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class HederaTokenInfoService implements NativeTokenDiscoveryService {
    private final Client hederaClient;
    private final MirrorNodeClient mirrorNodeClient;

    public HederaTokenInfoService(HederaClientFactory clientFactory,
                                  MirrorNodeClient mirrorNodeClient) {
        this.hederaClient = clientFactory.getClientWithOperator();
        this.mirrorNodeClient = mirrorNodeClient;
    }

    @Cacheable("hedera-token-info")
    public Optional<network.arkane.provider.token.TokenInfo> getTokenInfo(String tokenId) {
        return Optional.ofNullable(getTokenInfoFromChain(tokenId))
                       .map(t -> network.arkane.provider.token.TokenInfo.builder()
                                                                        .name(t.getName())
                                                                        .symbol(t.getSymbol())
                                                                        .decimals(t.getDecimals())
                                                                        .transferable(true)
                                                                        .address(tokenId)
                                                                        .type("TOKEN")
                                                                        .logo(getLogo(t))
                                                                        .build());
    }

    @Override
    public SecretType type() {
        return SecretType.HEDERA;
    }

    @Nullable
    private String getLogo(HederaTokenInfo tokenInfo) {
        String result = null;
        if (StringUtils.contains(tokenInfo.getTokenMemo(), "://")) {
            result = tokenInfo.getTokenMemo();
        } else if (StringUtils.contains(tokenInfo.getSymbol(), "://")) {
            result = tokenInfo.getSymbol();
        }
        return IpfsUtil.replaceIpfsLink(result);
    }

    private HederaTokenInfo getTokenInfoFromChain(String tokenId) {
        try {
            TokenInfo tokenInfo = new TokenInfoQuery()
                    .setTokenId(TokenId.fromString(tokenId))
                    .execute(hederaClient);
            return HederaTokenInfo.builder()
                                  .name(tokenInfo.name)
                                  .symbol(tokenInfo.symbol)
                                  .decimals(tokenInfo.decimals)
                                  .tokenMemo(tokenInfo.tokenMemo)
                                  .build();
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
