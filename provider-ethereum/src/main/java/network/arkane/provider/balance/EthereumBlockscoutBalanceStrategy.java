package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.blockchainproviders.blockscout.dto.ERC20BlockscoutToken;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(value = "blockscout.ethereum.url")
public class EthereumBlockscoutBalanceStrategy implements EthereumBalanceStrategy {

    private BlockscoutClient ethereumBlockscoutClient;
    private TokenDiscoveryService tokenDiscoveryService;

    public EthereumBlockscoutBalanceStrategy(BlockscoutClient ethereumBlockscoutClient,
                                             TokenDiscoveryService tokenDiscoveryService) {
        this.ethereumBlockscoutClient = ethereumBlockscoutClient;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public BigInteger getBalance(String account) {
        return ethereumBlockscoutClient.getBalance(account);
    }

    @Override
    public BigInteger getTokenBalance(String owner,
                                      String tokenAddress) {
        return ethereumBlockscoutClient.getTokenBalance(owner, tokenAddress);
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return ethereumBlockscoutClient.getTokenBalances(walletAddress)
                                       .stream()
                                       .filter(x -> x.getType().equalsIgnoreCase("erc-20"))
                                       .map(x -> (ERC20BlockscoutToken) x)
                                       .map(x -> TokenBalance.builder()
                                                             .tokenAddress(x.getContractAddress())
                                                             .rawBalance(x.getBalance().toString())
                                                             .balance(calculateBalance(x.getBalance(), x.getDecimals()))
                                                             .decimals(x.getDecimals())
                                                             .symbol(x.getSymbol())
                                                             .type(x.getType())
                                                             .transferable(true)
                                                             .logo(tokenDiscoveryService.getTokenLogo(SecretType.ETHEREUM, x.getContractAddress())
                                                                                        .orElse(null))
                                                             .build())
                                       .collect(Collectors.toList());
    }

    private Double calculateBalance(final BigInteger tokenBalance,
                                    Integer decimals) {
        if (decimals == null) {
            return null;
        }
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, decimals, RoundingMode.HALF_DOWN).doubleValue();
    }

}
