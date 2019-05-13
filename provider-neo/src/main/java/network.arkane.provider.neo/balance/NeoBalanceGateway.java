package network.arkane.provider.neo.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.neow3j.protocol.Neow3j;
import io.neow3j.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class NeoBalanceGateway extends BalanceGateway {

    private Neow3j NeoWeb3JGateway;
    private final TokenDiscoveryService tokenDiscoveryService;

    public NeoBalanceGateway(final Neow3j NeoWeb3JGateway,
                                  final TokenDiscoveryService tokenDiscoveryService) {
        this.NeoWeb3JGateway = NeoWeb3JGateway;
        this.tokenDiscoveryService = tokenDiscoveryService;
    }

    @Override
    public SecretType type() {
        return SecretType.NEO;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "Neo-node")
    public Balance getBalance(final String account) {
        try {
            final BigInteger balance = Numeric.decodeQuantity(NeoWeb3JGateway.getBalance(account).send().getBalance().getBalance()) ;
            return Balance.builder()
                    .available(true)
                    .rawBalance(balance.toString())
                    .rawGasBalance(balance.toString())
                    .secretType(SecretType.NEO)
                    .gasBalance(PrecisionUtil.toDecimal(balance, 18))
                    .balance(PrecisionUtil.toDecimal(balance, 18))
                    .symbol("NEO")
                    .gasSymbol("GAS")
                    .decimals(18)
                    .build();
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                    .message(String.format("Unable to get the balance for the specified account (%s)", account))
                    .errorCode("neow3j.internal-error")
                    .build();
        }
    }

    @Override
    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        final TokenInfo tokenInfo = tokenDiscoveryService.getTokenInfo(SecretType.NEO, tokenAddress).orElseThrow(IllegalArgumentException::new);
        return getTokenBalance(walletAddress, tokenInfo);
    }

    private TokenBalance getTokenBalance(final String walletAddress, final TokenInfo tokenInfo) {
        throw new UnsupportedOperationException("Not implemented yet for neo");
        /*final BigInteger tokenBalance =  NeoWeb3JGateway.getAccountState(walletAddress).send().getAccountState().getBalances().get(0);
        return TokenBalance.builder()
                .tokenAddress(tokenInfo.getAddress())
                .rawBalance(tokenBalance.toString())
                .balance(calculateBalance(tokenBalance, tokenInfo))
                .decimals(tokenInfo.getDecimals())
                .symbol(tokenInfo.getSymbol())
                .logo(tokenInfo.getLogo())
                .type(tokenInfo.getType())
                .transferable(tokenInfo.isTransferable())
                .build();*/
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalances(walletAddress, tokenDiscoveryService.getTokens(SecretType.NEO));
    }

    private List<TokenBalance> getTokenBalances(final String walletAddress, final List<TokenInfo> tokenInfo) {
        throw new UnsupportedOperationException("Not implemented yet for neo");
        /*final List<BigInteger> balances = NeoWeb3JGateway.getTokenBalances(walletAddress, tokenInfo.stream().map(TokenInfo::getAddress).collect(Collectors.toList()));
        final List<TokenBalance> results = new ArrayList<>();
        for (int i = 0; i < balances.size(); i++) {
            final TokenInfo token = tokenInfo.get(i);
            results.add(TokenBalance.builder()
                    .tokenAddress(token.getAddress())
                    .rawBalance(balances.get(i).toString())
                    .balance(calculateBalance(balances.get(i), token))
                    .decimals(token.getDecimals())
                    .symbol(token.getSymbol())
                    .type(token.getType())
                    .transferable(token.isTransferable())
                    .logo(token.getLogo())
                    .build());
        }
        return results;*/
    }

    private double calculateBalance(final BigInteger tokenBalance, final TokenInfo tokenInfo) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}