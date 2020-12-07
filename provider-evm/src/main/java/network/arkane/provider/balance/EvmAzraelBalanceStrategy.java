package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.token.erc20.Erc20TokenBalance;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.token.TokenDiscoveryProperties;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.jetbrains.annotations.NotNull;

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
                                    TokenDiscoveryProperties tokenDiscoveryProperties) {
        this.web3JGateway = web3JGateway;
        this.azraelClient = azraelClient;
        this.logoUrlPrefix = "https://img.arkane.network/" + tokenDiscoveryProperties.getPaths().get(type()) + "/logos/";
    }

    public abstract SecretType type();

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

    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        return getTokenBalances(walletAddress)
                .stream()
                .filter(b -> b.getTokenAddress().equalsIgnoreCase(tokenAddress))
                .findFirst()
                .orElse(null);
    }


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
                                                 .type("ERC_20")
                                                 .logo(getLogo(b))
                                                 .transferable(true)
                                                 .build())
                           .collect(Collectors.toList());
    }

    @NotNull
    private String getLogo(Erc20TokenBalance b) {
        return this.logoUrlPrefix.endsWith("/") ? this.logoUrlPrefix + b.getAddress() : this.logoUrlPrefix + "/" + b.getAddress() + ".png";
    }

    protected double calculateBalance(final BigInteger tokenBalance,
                                      Integer decimals) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
