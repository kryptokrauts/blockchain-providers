package network.arkane.provider.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Component
public class EthereumBalanceGateway extends BalanceGateway {

    private final TokenDiscoveryService tokenDiscoveryService;
    private final EthereumBalanceStrategy ethereumBalanceStrategy;

    public EthereumBalanceGateway(final TokenDiscoveryService tokenDiscoveryService,
                                  EthereumBalanceStrategy ethereumBalanceStrategy) {
        this.tokenDiscoveryService = tokenDiscoveryService;
        this.ethereumBalanceStrategy = ethereumBalanceStrategy;
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "ethereum-node")
    public Balance getBalance(final String account) {
        try {
            final BigInteger balance = ethereumBalanceStrategy.getBalance(account);

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

    private TokenBalance getTokenBalance(final String walletAddress,
                                         final TokenInfo tokenInfo) {
        final BigInteger tokenBalance = ethereumBalanceStrategy.getTokenBalance(walletAddress, tokenInfo.getAddress());
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
        return ethereumBalanceStrategy.getTokenBalances(walletAddress);
    }

    private double calculateBalance(final BigInteger tokenBalance,
                                    final TokenInfo tokenInfo) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, tokenInfo.getDecimals(), RoundingMode.HALF_DOWN).doubleValue();
    }
}
