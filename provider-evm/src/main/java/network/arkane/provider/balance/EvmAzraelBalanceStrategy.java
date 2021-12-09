package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.token.erc20.Erc20TokenBalance;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.token.TokenDiscoveryProperties;
import network.arkane.provider.web3j.EvmWeb3jGateway;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EvmAzraelBalanceStrategy implements EvmBalanceStrategy {

    private final EvmWeb3jGateway web3JGateway;
    private final AzraelClient azraelClient;
    private final String logoUrlPrefix;


    public EvmAzraelBalanceStrategy(final EvmWeb3jGateway web3JGateway,
                                    final AzraelClient azraelClient,
                                    final TokenDiscoveryProperties tokenDiscoveryProperties) {
        this.web3JGateway = web3JGateway;
        this.azraelClient = azraelClient;
        this.logoUrlPrefix = "https://img.arkane.network/" + tokenDiscoveryProperties.getPaths().get(type()) + "/logos/";
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
    public List<TokenBalance> getTokenBalances(final String walletAddress,
                                               final List<String> tokenAddresses) {
        final List<String> lowerCaseTokenAddresses = tokenAddresses.stream().map(String::toLowerCase).collect(Collectors.toList());
        return getTokenBalances(walletAddress).stream()
                                              .filter(b -> lowerCaseTokenAddresses.contains(b.getTokenAddress().toLowerCase()))
                                              .collect(Collectors.toList());
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return azraelClient.getTokens(walletAddress, Collections.singletonList(ContractType.ERC_20))
                           .stream()
                           .map(b -> (Erc20TokenBalance) b)
                           .map(b -> TokenBalance.builder()
                                                 .tokenAddress(b.getAddress())
                                                 .rawBalance(b.getBalance().toString())
                                                 .balance(calculateBalance(b.getBalance(), b.getDecimals()))
                                                 .decimals(b.getDecimals())
                                                 .symbol(b.getSymbol())
                                                 .name(b.getName())
                                                 .type("ERC_20")
                                                 .logo(getLogo(b.getAddress()))
                                                 .transferable(true)
                                                 .build())
                           .collect(Collectors.toList());
    }

    private String getLogo(final String tokenAddress) {
        return (this.logoUrlPrefix.endsWith("/") ? this.logoUrlPrefix + tokenAddress : this.logoUrlPrefix + "/" + tokenAddress) + ".png";
    }

    protected double calculateBalance(final BigInteger tokenBalance,
                                      Integer decimals) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
