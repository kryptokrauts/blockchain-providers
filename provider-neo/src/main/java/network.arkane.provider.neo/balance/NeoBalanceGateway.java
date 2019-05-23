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


@Slf4j
@Component
public class NeoBalanceGateway extends BalanceGateway {

    private NeoW3JGateway neoGate;
    private final TokenDiscoveryService service;

    public NeoBalanceGateway(final NeoW3JGateway neoGate, final TokenDiscoveryService service) {
        this.neoGate = neoGate;
        this.service = service;
    }

    @Override
    public SecretType type() {
        return SecretType.NEO;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "Neo-node")
    public Balance getBalance(final String account) {
        try {
            final List<NeoGetAccountState.Balance> balances = neoGate.getBalance(account);
            BigInteger neoBalance = BigInteger.ZERO;
            BigInteger gasBalance = BigInteger.ZERO;

            for (NeoGetAccountState.Balance item:balances
            ) {
                if(item.getAssetAddress().equals(NEOAsset.HASH_ID)){
                    neoBalance = NEOAsset.toBigInt(item.getValue());
                }else if(item.getAssetAddress().equals((GASAsset.HASH_ID))){
                    gasBalance = GASAsset.toBigInt(item.getValue());
                }
            }

            return Balance.builder()
                    .available(true)
                    .rawBalance(neoBalance.toString())
                    .rawGasBalance(gasBalance.toString())
                    .secretType(SecretType.NEO)
                    .balance(PrecisionUtil.toDecimal(neoBalance, 8))
                    .gasBalance(PrecisionUtil.toDecimal(gasBalance, 8))
                    .symbol(NEOAsset.NAME)
                    .gasSymbol(GASAsset.NAME)
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
    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        final TokenInfo tokenInfo = service.getTokenInfo(SecretType.NEO, tokenAddress).orElseThrow(IllegalArgumentException::new);
        return getTokenBalance(walletAddress, tokenInfo);
    }

    private TokenBalance getTokenBalance(final String walletAddress, final TokenInfo tokenInfo) {
        final BigInteger tokenBalance = neoGate.getTokenBalance(walletAddress,tokenInfo.getAddress());

        return TokenBalance.builder()
                .tokenAddress(tokenInfo.getAddress())
                .rawBalance(tokenBalance.toString())
                .balance(calculateBalance(tokenBalance, tokenInfo))
                .decimals(tokenInfo.getDecimals())
                .symbol(tokenInfo.getSymbol())
                .logo(tokenInfo.getLogo())
                .type(tokenInfo.getType())
                .transferable(tokenInfo.isTransferable())
                .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalances(walletAddress, service.getTokens(SecretType.NEO));
    }

    private List<TokenBalance> getTokenBalances(final String walletAddress, final List<TokenInfo> tokenInfo) {
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


}