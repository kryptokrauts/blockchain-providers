package network.arkane.provider.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EthereumBalanceGateway extends BalanceGateway {

    private EthereumWeb3JGateway ethereumWeb3JGateway;
    private final TokenDiscoveryService tokenDiscoveryService;

    public EthereumBalanceGateway(final EthereumWeb3JGateway ethereumWeb3JGateway,
                                  final TokenDiscoveryService tokenDiscoveryService) {
        this.ethereumWeb3JGateway = ethereumWeb3JGateway;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "ethereum-node")
    public Balance getBalance(final String account) {
        try {
            final BigInteger balance = ethereumWeb3JGateway.getBalance(account).getBalance();
            return Balance.builder()
                          .available(true)
                          .rawBalance(balance.toString())
                          .rawGasBalance(balance.toString())
                          .secretType(SecretType.ETHEREUM)
                          .gasBalance(PrecisionUtil.toDecimal(balance, 18))
                          .balance(PrecisionUtil.toDecimal(balance, 18))
                          .symbol("ETH")
                          .gasSymbol("ETH")
                          .decimals(18)
                          .build();
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .message(String.format("Unable to get the balance for the specified account (%s)", account))
                                 .errorCode("web3.internal-error")
                                 .build();
        }
    }

    @Override
    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        final TokenInfo tokenInfo = tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, tokenAddress).orElseThrow(IllegalArgumentException::new);
        return getTokenBalance(walletAddress, tokenInfo);
    }

    private TokenBalance getTokenBalance(final String walletAddress, final TokenInfo tokenInfo) {
        final BigInteger tokenBalance = ethereumWeb3JGateway.getTokenBalance(walletAddress, tokenInfo.getAddress());
        return TokenBalance.builder()
                           .tokenAddress(tokenInfo.getAddress())
                           .rawBalance(tokenBalance.toString())
                           .balance(calculateBalance(tokenBalance, tokenInfo))
                           .decimals(tokenInfo.getDecimals())
                           .symbol(tokenInfo.getSymbol())
                           .logo(tokenInfo.getLogo())
                           .type(tokenInfo.getType())
                           .transferable(tokenInfo.isTransferable())
                           .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalances(walletAddress, tokenDiscoveryService.getTokens(SecretType.ETHEREUM));
    }

    private List<TokenBalance> getTokenBalances(final String walletAddress, final List<TokenInfo> tokenInfo) {
        final List<BigInteger> balances = ethereumWeb3JGateway.getTokenBalances(walletAddress, tokenInfo.stream().map(TokenInfo::getAddress).collect(Collectors.toList()));
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

    private double calculateBalance(final BigInteger tokenBalance, final TokenInfo tokenInfo) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, tokenInfo.getDecimals(), RoundingMode.HALF_DOWN).doubleValue();
    }
}
