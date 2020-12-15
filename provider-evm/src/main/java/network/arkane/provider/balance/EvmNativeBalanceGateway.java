package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import network.arkane.provider.web3j.EvmWeb3jGateway;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EvmNativeBalanceGateway extends BalanceGateway {

    private EvmWeb3jGateway web3JGateway;
    private final TokenDiscoveryService tokenDiscoveryService;

    public EvmNativeBalanceGateway(final EvmWeb3jGateway web3JGateway,
                                   final TokenDiscoveryService tokenDiscoveryService) {
        this.web3JGateway = web3JGateway;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public Balance getBalance(final String account) {
        try {

            final BigInteger balance = web3JGateway.getBalance(account).getBalance();
            return Balance.builder()
                          .available(true)
                          .rawBalance(balance.toString())
                          .rawGasBalance(balance.toString())
                          .secretType(type())
                          .gasBalance(PrecisionUtil.toDecimal(balance, 18))
                          .balance(PrecisionUtil.toDecimal(balance, 18))
                          .symbol(type().getSymbol())
                          .gasSymbol(type().getGasSymbol())
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
        final TokenInfo tokenInfo = tokenDiscoveryService.getTokenInfo(type(), tokenAddress).orElseThrow(IllegalArgumentException::new);
        return getTokenBalance(walletAddress, tokenInfo);
    }

    private TokenBalance getTokenBalance(final String walletAddress,
                                         final TokenInfo tokenInfo) {
        final BigInteger tokenBalance = web3JGateway.getTokenBalance(walletAddress, tokenInfo.getAddress());
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
        return getTokenBalances(walletAddress, tokenDiscoveryService.getTokens(type()));
    }

    private List<TokenBalance> getTokenBalances(final String walletAddress,
                                                final List<TokenInfo> tokenInfo) {
        final List<BigInteger> balances = web3JGateway.getTokenBalances(walletAddress, tokenInfo.stream().map(TokenInfo::getAddress).collect(Collectors.toList()));
        final List<TokenBalance> results = new ArrayList<>();
        for (int i = 0; i < balances.size(); i++) {
            final TokenInfo token = tokenInfo.get(i);
            results.add(TokenBalance.builder()
                                    .tokenAddress(token.getAddress())
                                    .rawBalance(balances.get(i).toString())
                                    .balance(calculateBalance(balances.get(i), token.getDecimals()))
                                    .decimals(token.getDecimals())
                                    .symbol(token.getSymbol())
                                    .type(token.getType())
                                    .logo(token.getLogo())
                                    .transferable(token.isTransferable())
                                    .build());
        }
        return results;
    }

    protected double calculateBalance(final BigInteger tokenBalance,
                                      Integer decimals) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
