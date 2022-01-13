package network.arkane.provider.hedera.balance;

import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenId;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class HederaBalanceGateway extends BalanceGateway {

    private static final String FUNGIBLE_COMMON = "FUNGIBLE_COMMON";
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
            Hbar balance = getHbarBalanceFromChain(address);
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

    @Override
    public Balance getZeroBalance() {
        return Balance.builder()
                      .available(true)
                      .decimals(8)
                      .gasBalance(0.0)
                      .balance(0.0)
                      .rawGasBalance("0")
                      .rawBalance("0")
                      .secretType(SecretType.HEDERA)
                      .gasSymbol(SecretType.HEDERA.getGasSymbol())
                      .symbol(SecretType.HEDERA.getSymbol())
                      .build();
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
    public List<TokenBalance> getTokenBalances(String address) {
        return getTokenBalances(address, null);
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String address,
                                               final List<String> tokenAddresses) {
        final Map<TokenId, Long> tokenBalances = getTokenBalancesFromChain(address);
        final Set<String> lowerCaseTokenAddresses = tokenAddresses == null
                                                    ? null
                                                    : tokenAddresses.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return tokenBalances.entrySet()
                            .stream()
                            .filter(e -> lowerCaseTokenAddresses == null || lowerCaseTokenAddresses.contains(e.getKey().toString().toLowerCase()))
                            .map(e -> {
                                final Optional<TokenInfo> tokenInfo = tokenInfoService.getTokenInfo(e.getKey().toString());
                                if (tokenInfo.isPresent() && !FUNGIBLE_COMMON.equalsIgnoreCase(tokenInfo.get().getType())) return null;
                                return TokenBalance.builder()
                                                   .tokenAddress(e.getKey().toString())
                                                   .rawBalance(e.getValue().toString())
                                                   .balance(tokenInfo.map(info -> PrecisionUtil.toDecimal(e.getValue(), info.getDecimals()))
                                                                     .orElseGet(() -> e.getValue().doubleValue()))
                                                   .symbol(tokenInfo.map(TokenInfo::getSymbol).orElse("UNKNOWN"))
                                                   .logo(tokenInfo.map(TokenInfo::getLogo).orElse(""))
                                                   .name(tokenInfo.map(TokenInfo::getName).orElse("Unknown"))
                                                   .decimals(tokenInfo.map(TokenInfo::getDecimals).orElse(0))
                                                   .build();
                            }).filter(Objects::nonNull)
                            .collect(Collectors.toList());
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
