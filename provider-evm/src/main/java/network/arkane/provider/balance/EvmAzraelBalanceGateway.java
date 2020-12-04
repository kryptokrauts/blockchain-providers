package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.erc20.Erc20TokenBalance;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EvmAzraelBalanceGateway extends EvmNativeBalanceGateway {

    private final AzraelClient azraelClient;
    private final String logoUrlPrefix;


    public EvmAzraelBalanceGateway(final EvmWeb3jGateway web3JGateway,
                                   final TokenDiscoveryService tokenDiscoveryService,
                                   final AzraelClient azraelClient,
                                   String logoUrlPrefix) {
        super(web3JGateway, tokenDiscoveryService);
        this.azraelClient = azraelClient;
        this.logoUrlPrefix = logoUrlPrefix;
    }

    @Override
    public Balance getBalance(final String account) {
        return super.getBalance(account);
    }

    @Override
    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        return getTokenBalances(walletAddress)
                .stream()
                .filter(b -> b.getTokenAddress().equalsIgnoreCase(tokenAddress))
                .findFirst()
                .orElse(null);
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
                                                 .type("ERC_20")
                                                 .logo(getLogo(b))
                                                 .transferable(true)
                                                 .build())
                           .collect(Collectors.toList());
    }

    @NotNull
    private String getLogo(Erc20TokenBalance b) {
        return this.logoUrlPrefix.endsWith("/") ? this.logoUrlPrefix + b.getAddress() : this.logoUrlPrefix + "/" + b.getAddress();
    }

}
