package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.blockchainproviders.blockscout.dto.ERC20BlockscoutToken;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MaticBlockscoutBalanceStrategy implements MaticBalanceStrategy {

    private BlockscoutClient maticBlockscoutClient;
    private TokenDiscoveryService tokenDiscoveryService;

    public MaticBlockscoutBalanceStrategy(BlockscoutClient maticBlockscoutClient,
                                          TokenDiscoveryService tokenDiscoveryService) {
        this.maticBlockscoutClient = maticBlockscoutClient;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public BigInteger getBalance(String account) {
        return maticBlockscoutClient.getBalance(account);
    }

    @Override
    public BigInteger getTokenBalance(String owner,
                                      String tokenAddress) {
        return maticBlockscoutClient.getTokenBalance(owner, tokenAddress);
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return maticBlockscoutClient.getTokenBalances(walletAddress)
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
                                                          .logo(tokenDiscoveryService.getTokenLogo(SecretType.MATIC, x.getContractAddress())
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
