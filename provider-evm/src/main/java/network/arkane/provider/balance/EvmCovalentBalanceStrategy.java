package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.covalent.CovalentClient;
import network.arkane.blockchainproviders.covalent.dto.CovalentTokenBalanceResponse;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EvmCovalentBalanceStrategy implements EvmBalanceStrategy {

    private final EvmWeb3jGateway web3JGateway;
    private final CovalentClient covalentClient;
    private final String chainId;

    public EvmCovalentBalanceStrategy(final EvmWeb3jGateway web3JGateway,
                                      final CovalentClient covalentClient,
                                      final String chainId) {
        this.web3JGateway = web3JGateway;
        this.covalentClient = covalentClient;
        this.chainId = chainId;
    }

    @Override
    public Balance getBalance(final String account) {
        try {

            final BigInteger balance = web3JGateway.getBalance(account).getBalance();
            return Balance.builder()
                          .available(true)
                          .rawBalance(balance.toString())
                          .rawGasBalance(balance.toString())
                          .secretType(type())
                          .gasBalance(PrecisionUtil.toDecimal(balance, 18))
                          .balance(PrecisionUtil.toDecimal(balance, 18))
                          .symbol(type().getSymbol())
                          .gasSymbol(type().getGasSymbol())
                          .decimals(18)
                          .build();
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .message(String.format("Unable to get the balance for the specified account (%s)", account))
                                 .errorCode("web3.internal-error")
                                 .build();
        }
    }

    @Override
    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        return getTokenBalances(walletAddress)
                .stream()
                .filter(tb -> tb.getTokenAddress().equalsIgnoreCase(tokenAddress))
                .findFirst().orElse(null);
    }


    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        CovalentTokenBalanceResponse tokenBalances = covalentClient.getTokenBalances(chainId, walletAddress);
        if (tokenBalances != null && tokenBalances.getData() != null && CollectionUtils.isNotEmpty(tokenBalances.getData().getItems())) {
            return tokenBalances
                    .getData()
                    .getItems()
                    .stream()
                    .filter(i -> !(chainId.equalsIgnoreCase("1") && i.getTokenAddress().equalsIgnoreCase("0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")))
                    .filter(i -> !(chainId.equalsIgnoreCase("56") && i.getSymbol().equalsIgnoreCase("BNB")))
                    .filter(i -> !(chainId.equalsIgnoreCase("80001") && i.getTokenAddress().equalsIgnoreCase("0x0000000000000000000000000000000000001010")))
                    .filter(i -> !(chainId.equalsIgnoreCase("137") && i.getTokenAddress().equalsIgnoreCase("0x0000000000000000000000000000000000001010")))
                    .map(i -> TokenBalance
                            .builder()
                            .tokenAddress(i.getTokenAddress())
                            .rawBalance(i.getBalance().toString())
                            .balance(calculateBalance(i.getBalance(), i.getContractDecimals()))
                            .decimals(i.getContractDecimals())
                            .symbol(i.getSymbol())
                            .logo(i.getLogo())
                            .type("ERC20")
                            .transferable(true)
                            .build())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    protected double calculateBalance(final BigInteger tokenBalance,
                                      Integer decimals) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
