package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnMissingBean(value = MaticBlockscoutBalanceStrategy.class)
public class MaticNativeBalanceStrategy implements MaticBalanceStrategy {

    private MaticWeb3JGateway maticWeb3JGateway;
    private TokenDiscoveryService tokenDiscoveryService;

    public MaticNativeBalanceStrategy(MaticWeb3JGateway maticWeb3JGateway,
                                      TokenDiscoveryService tokenDiscoveryService) {
        this.maticWeb3JGateway = maticWeb3JGateway;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public BigInteger getBalance(String account) {
        return maticWeb3JGateway.getBalance(account).getBalance();
    }

    @Override
    public BigInteger getTokenBalance(String owner,
                                      String tokenAddress) {
        return maticWeb3JGateway.getTokenBalance(owner, tokenAddress);
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalances(walletAddress, tokenDiscoveryService.getTokens(SecretType.MATIC));
    }

    private List<TokenBalance> getTokenBalances(final String walletAddress,
                                                final List<TokenInfo> tokenInfo) {
        final List<BigInteger> balances = maticWeb3JGateway.getTokenBalances(walletAddress, tokenInfo.stream().map(TokenInfo::getAddress).collect(Collectors.toList()));
        final List<TokenBalance> results = new ArrayList<>();
        for (int i = 0; i < balances.size(); i++) {
            final TokenInfo token = tokenInfo.get(i);
            results.add(TokenBalance.builder()
                                    .tokenAddress(token.getAddress())
                                    .rawBalance(balances.get(i).toString())
                                    .balance(calculateBalance(balances.get(i), token))
                                    .decimals(token.getDecimals())
                                    .symbol(token.getSymbol())
                                    .type(token.getType())
                                    .transferable(token.isTransferable())
                                    .logo(token.getLogo())
                                    .build());
        }
        return results;
    }

    private double calculateBalance(final BigInteger tokenBalance,
                                    final TokenInfo tokenInfo) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, tokenInfo.getDecimals(), RoundingMode.HALF_DOWN).doubleValue();
    }
}
