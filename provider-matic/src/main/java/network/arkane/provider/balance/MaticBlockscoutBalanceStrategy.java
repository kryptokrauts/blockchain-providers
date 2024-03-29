package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.blockchainproviders.blockscout.dto.ERC20BlockscoutToken;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.threading.Threading;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name = "matic.balance.strategy", havingValue = "blockscout")
public class MaticBlockscoutBalanceStrategy implements EvmBalanceStrategy {

    private BlockscoutClient maticBlockscoutClient;
    private TokenDiscoveryService tokenDiscoveryService;

    public MaticBlockscoutBalanceStrategy(BlockscoutClient maticBlockscoutClient,
                                          TokenDiscoveryService tokenDiscoveryService) {
        this.maticBlockscoutClient = maticBlockscoutClient;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public Balance getBalance(String account) {
        BigInteger bal = maticBlockscoutClient.getBalance(account);
        return Balance.builder()
                      .available(true)
                      .rawBalance(bal.toString())
                      .rawGasBalance(bal.toString())
                      .secretType(SecretType.MATIC)
                      .gasBalance(PrecisionUtil.toDecimal(bal, 18))
                      .balance(PrecisionUtil.toDecimal(bal, 18))
                      .symbol("MATIC")
                      .gasSymbol("MATIC")
                      .decimals(18)
                      .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return maticBlockscoutClient.getTokenBalances(walletAddress)
                                    .stream()
                                    .filter(x -> "erc-20".equalsIgnoreCase(x.getType()))
                                    .map(x -> (ERC20BlockscoutToken) x)
                                    .map(x -> TokenBalance.builder()
                                                          .tokenAddress(x.getContractAddress())
                                                          .rawBalance(x.getBalance() == null ? "0" : x.getBalance().toString())
                                                          .balance(calculateBalance(x.getBalance(), x.getDecimals()))
                                                          .decimals(x.getDecimals())
                                                          .symbol(x.getSymbol())
                                                          .name(x.getName())
                                                          .type(x.getType())
                                                          .transferable(true)
                                                          .logo(tokenDiscoveryService.getTokenLogo(SecretType.MATIC, x.getContractAddress())
                                                                                     .orElse(null))
                                                          .build())
                                    .collect(Collectors.toList());
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress,
                                               final List<String> tokenAddresses) {
        if (CollectionUtils.isEmpty(tokenAddresses)) return getTokenBalances(walletAddress);
        final List<TokenInfo> tokenInfos = Threading.runInThreadPool("getTokenInfos",
                                                                     () -> tokenAddresses.parallelStream()
                                                                                         .map(tokenAddress -> tokenDiscoveryService.getTokenInfo(type(), tokenAddress)
                                                                                                                                   .orElse(null))
                                                                                         .filter(Objects::nonNull)
                                                                                         .collect(Collectors.toList()));
        final Map<String, TokenInfo> tokenInfoMap = tokenInfos.stream().collect(Collectors.toMap(ti -> ti.getAddress().toLowerCase(), ti -> ti));
        return maticBlockscoutClient.getTokenBalances(walletAddress, tokenAddresses)
                                    .entrySet()
                                    .stream()
                                    .map(entry -> {
                                        final TokenInfo tokenInfo = tokenInfoMap.get(entry.getKey());
                                        final BigInteger balance = entry.getValue();
                                        return TokenBalance.builder()
                                                           .tokenAddress(tokenInfo.getAddress())
                                                           .rawBalance(balance.toString())
                                                           .balance(calculateBalance(balance, tokenInfo.getDecimals()))
                                                           .decimals(tokenInfo.getDecimals())
                                                           .symbol(tokenInfo.getSymbol())
                                                           .name(tokenInfo.getName())
                                                           .logo(tokenInfo.getLogo())
                                                           .type(tokenInfo.getType())
                                                           .transferable(tokenInfo.isTransferable())
                                                           .build();
                                    })
                                    .collect(Collectors.toList());
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }

    private Double calculateBalance(final BigInteger tokenBalance,
                                    Integer decimals) {
        if (decimals == null || tokenBalance == null) {
            return (double) 0;
        }
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, decimals, RoundingMode.HALF_DOWN).doubleValue();
    }

}
