package network.arkane.provider.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.core.model.blockchain.Account;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Amount;
import network.arkane.provider.core.model.clients.ERC20Token;
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
        return SecretType.VECHAIN;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "vechain-node")
    public Balance getBalance(final String hexAccount) {
        final Account account = vechainGateway.getAccount(hexAccount);
        return Balance.builder()
                      .available(true)
                      .secretType(SecretType.VECHAIN)
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
    public TokenBalance getTokenBalance(final String walletAddress, final String tokenAddress) {
        final TokenInfo tokenInfo = tokenDiscoveryService.getTokenInfo(SecretType.VECHAIN, tokenAddress).orElseThrow(IllegalArgumentException::new);
        return getTokenBalance(walletAddress, tokenInfo);
    }


    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalances(walletAddress, tokenDiscoveryService.getTokens(SecretType.VECHAIN));
    }

    private List<TokenBalance> getTokenBalances(final String walletAddress, final List<TokenInfo> tokenInfo) {
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

    private double calculateBalance(final BigInteger tokenBalance, final TokenInfo tokenInfo) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }

    private TokenBalance getTokenBalance(final String walletAddress, final TokenInfo tokenInfo) {
        final ERC20Token token = ERC20Token.create(tokenInfo.getName(), Address.fromHexString(tokenInfo.getAddress()));
        token.setPrecision(new BigDecimal(tokenInfo.getDecimals()));
        Amount tokenBalance = vechainGateway.getTokenBalance(walletAddress, token);
        return TokenBalance.builder()
                           .tokenAddress(tokenInfo.getAddress())
                           .balance(tokenBalance.getAmount() == null ? 0 : tokenBalance.getAmount().doubleValue())
                           .rawBalance(tokenBalance.getAmount() == null ? "0" : tokenBalance.toBigInteger().toString())
                           .decimals(tokenInfo.getDecimals())
                           .symbol(tokenInfo.getSymbol())
                           .name(tokenInfo.getName())
                           .type(tokenInfo.getType())
                           .logo(tokenInfo.getLogo())
                           .transferable(tokenInfo.isTransferable())
                           .build();
    }
}
