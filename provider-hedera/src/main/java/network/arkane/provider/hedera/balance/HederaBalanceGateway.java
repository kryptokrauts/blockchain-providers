package network.arkane.provider.hedera.balance;

import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfo;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.hedera.HederaClientFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class HederaBalanceGateway extends BalanceGateway {

    private final Client hederaClient;
    private final HederaTokenInfoService tokenInfoService;

    public HederaBalanceGateway(HederaClientFactory clientFactory,
                                HederaTokenInfoService tokenInfoService) {
        this.hederaClient = clientFactory.getClientWithOperator();
        this.tokenInfoService = tokenInfoService;
    }

    @Override
    public Balance getBalance(String address) {
        try {
            Hbar balance = new AccountBalanceQuery()
                    .setAccountId(AccountId.fromString(address))
                    .execute(hederaClient)
                    .hbars;
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
        } catch (TimeoutException | PrecheckStatusException e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.balance.error")
                                 .build();
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
            Map<TokenId, Long> tokenBalances = new AccountBalanceQuery()
                    .setAccountId(AccountId.fromString(address))
                    .execute(hederaClient)
                    .token;
            for (Map.Entry<TokenId, Long> entry : tokenBalances.entrySet()) {
                new TokenInfoQuery().setTokenId(entry.getKey()).execute(hederaClient);
            }
            return tokenBalances.entrySet().stream()
                                .filter(e -> e.getKey().toString().equalsIgnoreCase(tokenAddress) || StringUtils.isBlank(tokenAddress))
                                .map(e -> {
                                    TokenInfo tokenInfo = tokenInfoService.getTokenInfo(e.getKey().toString());
                                    return TokenBalance.builder()
                                                       .tokenAddress(e.getKey().toString())
                                                       .rawBalance(e.getValue().toString())
                                                       .balance(PrecisionUtil.toDecimal(e.getValue(), tokenInfo.decimals))
                                                       .symbol(tokenInfo.symbol)
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

    @Override
    public SecretType type() {
        return SecretType.HEDERA;
    }
}
