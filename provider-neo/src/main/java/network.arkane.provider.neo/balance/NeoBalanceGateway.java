package network.arkane.provider.neo.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.neow3j.model.types.GASAsset;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static network.arkane.provider.chain.SecretType.NEO;


@Slf4j
@Component
public class NeoBalanceGateway extends BalanceGateway {

    private NeoW3JGateway neoGate;
    private final TokenDiscoveryService service;

    public NeoBalanceGateway(final NeoW3JGateway neoGate,
                             final TokenDiscoveryService service) {
        this.neoGate = neoGate;
        this.service = service;
    }

    @Override
    public SecretType type() {
        return NEO;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "Neo-node")
    public Balance getBalance(final String account) {
        try {
            final List<NeoGetAccountState.Balance> balances = neoGate.getBalance(account);
            BigInteger neoBalance = BigInteger.ZERO;
            BigInteger gasBalance = BigInteger.ZERO;

            for (NeoGetAccountState.Balance item : balances
            ) {
                if (item.getAssetAddress().equals("0x" + NEOAsset.HASH_ID)) {
                    neoBalance = new BigInteger(item.getValue());
                } else if (item.getAssetAddress().equals(("0x" + GASAsset.HASH_ID))) {
                    gasBalance = GASAsset.toBigInt(item.getValue());
                }
            }

            return Balance.builder()
                          .available(true)
                          .rawBalance(neoBalance.toString())
                          .rawGasBalance(gasBalance.toString())
                          .secretType(NEO)
                          .balance(PrecisionUtil.toDecimal(neoBalance, 0))
                          .gasBalance(PrecisionUtil.toDecimal(gasBalance, 8))
                          .symbol(NEOAsset.NAME)
                          .gasSymbol("GAS")
                          .decimals(8)
                          .build();
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .message(String.format("Unable to get the balance for the specified account (%s)", account))
                                 .errorCode("neow3j.internal-error")
                                 .build();
        }
    }

    @Override
    public Balance getZeroBalance() {
        return Balance.builder()
                      .available(true)
                      .rawBalance("0")
                      .rawGasBalance("0")
                      .secretType(SecretType.NEO)
                      .balance(0.0)
                      .gasBalance(0.0)
                      .symbol(NEOAsset.NAME)
                      .gasSymbol("GAS")
                      .decimals(8)
                      .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress,
                                               final List<String> tokenAddresses) {
        final List<TokenInfo> tokenInfos = tokenAddresses.stream()
                                                         .map(tokenAddress -> service.getTokenInfo(NEO, tokenAddress)
                                                                                     .orElseThrow(IllegalArgumentException::new))
                                                         .collect(Collectors.toList());
        return getTokenBalancesForTokenInfos(walletAddress, tokenInfos);
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalancesForTokenInfos(walletAddress, service.getTokens(NEO));
    }

    private List<TokenBalance> getTokenBalancesForTokenInfos(final String walletAddress,
                                                             final List<TokenInfo> tokenInfo) {
        final List<BigInteger> balances = neoGate.getTokenBalances(walletAddress, tokenInfo.stream().map(TokenInfo::getAddress).collect(Collectors.toList()));
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
