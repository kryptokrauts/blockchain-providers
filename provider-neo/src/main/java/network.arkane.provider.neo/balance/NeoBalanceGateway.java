package network.arkane.provider.neo.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.neow3j.model.types.GASAsset;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
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

import java.io.IOException;
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
            final List<NeoGetAccountState.Balance> balances = NeoWeb3JGateway.getAccountState(account).send().getAccountState().getBalances();
            String neoBalance= "0";
            String gasBalance= "0";

            for (NeoGetAccountState.Balance item:balances
                 ) {
                if(item.getAssetAddress().equals(NEOAsset.HASH_ID)){
                    neoBalance = item.getValue();
                }else if(item.getAssetAddress().equals((GASAsset.HASH_ID))){
                    gasBalance =item.getValue();
                }
            }

            return Balance.builder()
                    .available(true)
                    .rawBalance(neoBalance)
                    .rawGasBalance(gasBalance)
                    .secretType(SecretType.NEO)
                    .gasBalance(PrecisionUtil.toDecimal(NEOAsset.toBigInt(neoBalance), 10))
                    .balance(PrecisionUtil.toDecimal(GASAsset.toBigInt(gasBalance), 10))
                    .symbol(NEOAsset.NAME)
                    .gasSymbol(GASAsset.NAME)
                    .decimals(10)
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
//        final BigInteger tokenBalance;
//        try {
//            tokenBalance = NeoWeb3JGateway.invokeFunction(tokenInfo.getAddress(),"balanceOf" ).send().getResult().getStack().;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return TokenBalance.builder()
//                .tokenAddress(tokenInfo.getAddress())
//                .rawBalance(tokenBalance.toString())
//                .balance(calculateBalance(tokenBalance, tokenInfo))
//                .decimals(tokenInfo.getDecimals())
//                .symbol(tokenInfo.getSymbol())
//                .logo(tokenInfo.getLogo())
//                .type(tokenInfo.getType())
//                .transferable(tokenInfo.isTransferable())
//                .build();
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