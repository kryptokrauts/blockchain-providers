package network.arkane.provider.hedera.balance;

import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.balance.dto.HederaTokenInfo;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class HederaBalanceGateway extends BalanceGateway {

    private final Client hederaClient;
    private final HederaTokenInfoService tokenInfoService;
    private final MirrorNodeClient mirrorNodeClient;

    public HederaBalanceGateway(HederaClientFactory clientFactory,
                                HederaTokenInfoService tokenInfoService,
                                MirrorNodeClient mirrorNodeClient) {
        this.hederaClient = clientFactory.getClientWithOperator();
        this.tokenInfoService = tokenInfoService;
        this.mirrorNodeClient = mirrorNodeClient;
    }

    @Override
    public Balance getBalance(String address) {
        try {
            Hbar balance = getHbarBalanceFromMirrorNode(address).orElseGet(() -> getHbarBalanceFromChain(address));
            return Balance.builder()
                          .available(true)
                          .decimals(8)
                          .gasBalance(balance.getValue().doubleValue())
                          .balance(balance.getValue().doubleValue())
                          .rawGasBalance("" + balance.toTinybars())
                          .rawBalance("" + balance.toTinybars())
                          .secretType(SecretType.HEDERA)
                          .gasSymbol(SecretType.HEDERA.getGasSymbol())
                          .symbol(SecretType.HEDERA.getSymbol())
                          .build();
        } catch (Exception e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.balance.error")
                                 .build();
        }
    }

    private Hbar getHbarBalanceFromChain(String address) {

        try {
            return new AccountBalanceQuery()
                    .setAccountId(AccountId.fromString(address))
                    .execute(hederaClient)
                    .hbars;
        } catch (TimeoutException | PrecheckStatusException e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.balance.error")
                                 .build();
        }

    }

    private Optional<Hbar> getHbarBalanceFromMirrorNode(String address) throws TimeoutException, PrecheckStatusException {
        try {
            return mirrorNodeClient.getBalance(address).getBalances()
                                   .stream()
                                   .map(b -> Hbar.fromTinybars(b.getBalance()))
                                   .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public TokenBalance getTokenBalance(String address,
                                        String tokenAddress) {
        List<TokenBalance> tokenBalances = getTokenBalances(address, tokenAddress);

        return tokenBalances.size() > 0
               ? tokenBalances.get(0)
               : null;
    }

    @Override
    public List<TokenBalance> getTokenBalances(String address) {
        return getTokenBalances(address, null);
    }


    private List<TokenBalance> getTokenBalances(String address,
                                                String tokenAddress) {
        try {
            Map<TokenId, Long> tokenBalances = getTokenBalancesFromMirrorNode(address).orElseGet(() -> getTokenBalancesFromChain(address));
            for (Map.Entry<TokenId, Long> entry : tokenBalances.entrySet()) {
                new TokenInfoQuery().setTokenId(entry.getKey()).execute(hederaClient);
            }
            return tokenBalances.entrySet().stream()
                                .filter(e -> e.getKey().toString().equalsIgnoreCase(tokenAddress) || StringUtils.isBlank(tokenAddress))
                                .map(e -> {
                                    HederaTokenInfo tokenInfo = tokenInfoService.getTokenInfo(e.getKey().toString());
                                    return TokenBalance.builder()
                                                       .tokenAddress(e.getKey().toString())
                                                       .rawBalance(e.getValue().toString())
                                                       .balance(PrecisionUtil.toDecimal(e.getValue(), tokenInfo.getDecimals()))
                                                       .symbol(tokenInfo.getSymbol().contains("://")
                                                               ? null
                                                               : tokenInfo.getSymbol())
                                                       .logo(tokenInfo.getSymbol().startsWith("http") ? tokenInfo.getSymbol() : null)
                                                       .name(tokenInfo.getName())
                                                       .decimals(tokenInfo.getDecimals())
                                                       .build();
                                })
                                .collect(Collectors.toList());

        } catch (TimeoutException | PrecheckStatusException e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.balance.error")
                                 .build();
        }
    }

    private Map<TokenId, Long> getTokenBalancesFromChain(String address) {
        try {
            return new AccountBalanceQuery()
                    .setAccountId(AccountId.fromString(address))
                    .execute(hederaClient)
                    .tokens;
        } catch (TimeoutException | PrecheckStatusException e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.balance.error")
                                 .build();
        }
    }

    private Optional<Map<TokenId, Long>> getTokenBalancesFromMirrorNode(String address) throws TimeoutException, PrecheckStatusException {
        try {
            return Optional.of(mirrorNodeClient.getBalance(address)
                                               .getBalances()
                                               .stream()
                                               .flatMap(ab -> ab.getTokens().stream())
                                               .collect(Collectors.toMap(a -> TokenId.fromString(a.getTokenId()), a -> a.getBalance()))
                              );
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public SecretType type() {
        return SecretType.HEDERA;
    }
}
