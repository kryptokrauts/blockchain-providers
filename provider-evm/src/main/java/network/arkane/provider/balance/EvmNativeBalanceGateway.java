package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.threading.Threading;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Balance getZeroBalance() {
        return Balance.builder()
                      .available(true)
                      .rawBalance("0")
                      .rawGasBalance("0")
                      .secretType(type())
                      .gasBalance(0.0)
                      .balance(0.0)
                      .symbol(type().getSymbol())
                      .gasSymbol(type().getGasSymbol())
                      .decimals(18)
                      .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalancesForTokenInfos(walletAddress, tokenDiscoveryService.getTokens(type()));
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress,
                                               final List<String> tokenAddresses) {
        if (CollectionUtils.isEmpty(tokenAddresses)) return getTokenBalances(walletAddress);
        final List<TokenInfo> tokenInfoList = Threading.runInThreadPool("getTokenInfos",
                                                                     () -> tokenAddresses.parallelStream()
                                                                                         .map(tokenAddress -> tokenDiscoveryService.getTokenInfo(type(), tokenAddress)
                                                                                                                                   .orElse(null))
                                                                                         .filter(Objects::nonNull)
                                                                                         .collect(Collectors.toList()));
        return getTokenBalancesForTokenInfos(walletAddress, tokenInfoList);
    }

    private List<TokenBalance> getTokenBalancesForTokenInfos(final String walletAddress,
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
                                    .name(token.getName())
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
