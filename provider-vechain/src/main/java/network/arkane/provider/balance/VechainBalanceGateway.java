package network.arkane.provider.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.core.model.blockchain.Account;
import network.arkane.provider.gateway.VechainGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static network.arkane.provider.chain.SecretType.VECHAIN;

@Component
public class VechainBalanceGateway extends BalanceGateway {

    private VechainGateway vechainGateway;
    private final TokenDiscoveryService tokenDiscoveryService;

    public VechainBalanceGateway(final VechainGateway vechainGateway,
                                 final TokenDiscoveryService tokenDiscoveryService) {
        this.vechainGateway = vechainGateway;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public SecretType type() {
        return VECHAIN;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "vechain-node")
    public Balance getBalance(final String hexAccount) {
        final Account account = vechainGateway.getAccount(hexAccount);
        return Balance.builder()
                      .available(true)
                      .secretType(VECHAIN)
                      .balance(account.VETBalance().getAmount().doubleValue())
                      .rawBalance(account.VETBalance().toBigInteger().toString())
                      .gasBalance(account.energyBalance().getAmount().doubleValue())
                      .rawGasBalance(account.energyBalance().toBigInteger().toString())
                      .symbol("VET")
                      .gasSymbol("VTHO")
                      .decimals(18)
                      .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress,
                                               final List<String> tokenAddresses) {
        final List<TokenInfo> tokenInfos = tokenAddresses.stream()
                                                         .map(tokenAddress -> tokenDiscoveryService.getTokenInfo(VECHAIN, tokenAddress).orElseThrow(IllegalArgumentException::new))
                                                         .collect(Collectors.toList());
        return getTokenBalancesForTokenInfos(walletAddress, tokenInfos);
    }


    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalancesForTokenInfos(walletAddress, tokenDiscoveryService.getTokens(VECHAIN));
    }

    private List<TokenBalance> getTokenBalancesForTokenInfos(final String walletAddress,
                                                             final List<TokenInfo> tokenInfo) {
        final List<BigInteger> balances = vechainGateway.getTokenBalances(walletAddress, tokenInfo.stream().map(TokenInfo::getAddress).collect(Collectors.toList()));
        final List<TokenBalance> results = new ArrayList<>();
        for (int i = 0; i < balances.size(); i++) {
            final TokenInfo token = tokenInfo.get(i);
            results.add(TokenBalance.builder()
                                    .tokenAddress(token.getAddress())
                                    .rawBalance(balances.get(i).toString())
                                    .balance(calculateBalance(balances.get(i), token))
                                    .decimals(token.getDecimals())
                                    .symbol(token.getSymbol())
                                    .name(token.getName())
                                    .type(token.getType())
                                    .transferable(token.isTransferable())
                                    .logo(token.getLogo())
                                    .build());
        }
        return results;
    }

    private double calculateBalance(final BigInteger tokenBalance,
                                    final TokenInfo tokenInfo) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
