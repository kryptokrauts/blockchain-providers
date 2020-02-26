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
public class MaticBalanceGateway extends BalanceGateway {

    public static final String ERC_20 = "ERC-20";
    private MaticBalanceStrategy maticBalanceStrategy;
    private final TokenDiscoveryService tokenDiscoveryService;

    public MaticBalanceGateway(final MaticBalanceStrategy maticBalanceStrategy,
                               final TokenDiscoveryService tokenDiscoveryService) {
        this.maticBalanceStrategy = maticBalanceStrategy;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "matic-node")
    public Balance getBalance(final String account) {
        try {
            final BigInteger balance = maticBalanceStrategy.getBalance(account);
            return Balance.builder()
                          .available(true)
                          .rawBalance(balance.toString())
                          .rawGasBalance(balance.toString())
                          .secretType(SecretType.MATIC)
                          .gasBalance(PrecisionUtil.toDecimal(balance, 18))
                          .balance(PrecisionUtil.toDecimal(balance, 18))
                          .symbol("MATIC")
                          .gasSymbol("MATIC")
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
        final TokenInfo tokenInfo = tokenDiscoveryService.getTokenInfo(SecretType.MATIC, tokenAddress).orElseThrow(IllegalArgumentException::new);
        return getTokenBalance(walletAddress, tokenInfo);
    }

    private TokenBalance getTokenBalance(final String walletAddress,
                                         final TokenInfo tokenInfo) {
        final BigInteger tokenBalance = maticBalanceStrategy.getTokenBalance(walletAddress, tokenInfo.getAddress());
        return TokenBalance.builder()
                           .tokenAddress(tokenInfo.getAddress())
                           .rawBalance(tokenBalance.toString())
                           .balance(calculateBalance(tokenBalance, tokenInfo.getDecimals()))
                           .decimals(tokenInfo.getDecimals())
                           .symbol(tokenInfo.getSymbol())
                           .logo(tokenInfo.getLogo())
                           .type(tokenInfo.getType())
                           .transferable(tokenInfo.isTransferable())
                           .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return maticBalanceStrategy.getTokenBalances(walletAddress);
    }


    private double calculateBalance(final BigInteger tokenBalance,
                                    final int decimals) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, decimals, RoundingMode.HALF_DOWN).doubleValue();
    }
}
