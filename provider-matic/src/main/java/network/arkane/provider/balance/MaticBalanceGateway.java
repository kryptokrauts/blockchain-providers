package network.arkane.provider.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MaticBalanceGateway extends BalanceGateway {

    public static final String ERC_20 = "ERC-20";
    private MaticWeb3JGateway maticWeb3JGateway;
    private final MaticBlockscoutDiscoveryService maticBlockscoutDiscoveryService;

    public MaticBalanceGateway(final MaticWeb3JGateway maticWeb3JGateway,
                               final MaticBlockscoutDiscoveryService maticBlockscoutDiscoveryService) {
        this.maticWeb3JGateway = maticWeb3JGateway;
        this.maticBlockscoutDiscoveryService = maticBlockscoutDiscoveryService;
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "matic-node")
    public Balance getBalance(final String account) {
        try {
            final BigInteger balance = maticWeb3JGateway.getBalance(account).getBalance();
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
        final TokenInfo tokenInfo = maticBlockscoutDiscoveryService.getTokenInfo(tokenAddress).orElseThrow(IllegalArgumentException::new);
        return getTokenBalance(walletAddress, tokenInfo);
    }

    private TokenBalance getTokenBalance(final String walletAddress,
                                         final TokenInfo tokenInfo) {
        final BigInteger tokenBalance = maticWeb3JGateway.getTokenBalance(walletAddress, tokenInfo.getAddress());
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
        final List<MaticBlockscoutTokenResponse> tokens = maticBlockscoutDiscoveryService.getTokens(walletAddress);
        return tokens.stream()
                     .filter(x -> x.getType().equals(ERC_20))
                     .map(token -> {
                         Optional<TokenInfo> tokenInfo = maticBlockscoutDiscoveryService.getTokenInfo(token.getContractAddress());
                         return TokenBalance.builder()
                                            .balance(calculateBalance(token.getBalance(), token.getDecimals()))
                                            .rawBalance(token.getBalance().toString())
                                            .tokenAddress(token.getContractAddress())
                                            .decimals(token.getDecimals())
                                            .symbol(token.getSymbol())
                                            .type(token.getType())
                                            .transferable(tokenInfo.map(TokenInfo::isTransferable).orElse(true))
                                            .logo(tokenInfo.map(TokenInfo::getLogo).orElse(""))
                                            .build();
                     }).collect(Collectors.toList());
    }


    private double calculateBalance(final BigInteger tokenBalance,
                                    final int decimals) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
