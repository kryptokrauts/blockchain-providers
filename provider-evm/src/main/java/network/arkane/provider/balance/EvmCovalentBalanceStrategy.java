package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.covalent.CovalentClient;
import network.arkane.blockchainproviders.covalent.dto.CovalentItem;
import network.arkane.blockchainproviders.covalent.dto.CovalentTokenBalanceResponse;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
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
    private final TokenDiscoveryService tokenDiscoveryService;

    public EvmCovalentBalanceStrategy(final EvmWeb3jGateway web3JGateway,
                                      final TokenDiscoveryService tokenDiscoveryService,
                                      final CovalentClient covalentClient,
                                      final String chainId) {
        this.web3JGateway = web3JGateway;
        this.tokenDiscoveryService = tokenDiscoveryService;
        this.covalentClient = covalentClient;
        this.chainId = chainId;
    }

    @Override
    public Balance getBalance(final String account) {
        try {
            return getNativeBalanceFromNode(account);
        } catch (final Exception ex) {
            log.warn(String.format("Fetching native balance from node failed for account: %s. Trying Covalent as fallback", account), ex);
            try {
                return getNativeBalanceFromCovalent(account);
            } catch (Exception ex2) {
                throw ArkaneException.arkaneException()
                                     .message(String.format("Unable to get the balance for the specified account (%s)", account))
                                     .errorCode("web3.internal-error")
                                     .build();
            }
        }
    }

    private Balance getNativeBalanceFromNode(String account) {
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
    }

    private Balance getNativeBalanceFromCovalent(String account) {
        CovalentTokenBalanceResponse tokenBalances = covalentClient.getTokenBalances(chainId, account);
        if (tokenBalances != null && tokenBalances.getData() != null && CollectionUtils.isNotEmpty(tokenBalances.getData().getItems())) {
            return tokenBalances
                    .getData()
                    .getItems()
                    .stream()
                    .filter(this::isNativeToken)
                    .findFirst()
                    .map(i -> Balance.builder()
                                     .available(true)
                                     .rawBalance(i.getBalance().toString())
                                     .rawGasBalance(i.getBalance().toString())
                                     .secretType(type())
                                     .gasBalance(PrecisionUtil.toDecimal(i.getBalance(), 18))
                                     .balance(PrecisionUtil.toDecimal(i.getBalance(), 18))
                                     .symbol(type().getSymbol())
                                     .gasSymbol(type().getGasSymbol())
                                     .decimals(18)
                                     .build())
                    .orElseThrow();
        }
        throw new RuntimeException("No native balance found");
    }

    @Override
    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        try {
            final TokenInfo tokenInfo = tokenDiscoveryService.getTokenInfo(type(), tokenAddress).orElseThrow(IllegalArgumentException::new);
            final BigInteger tokenBalance = web3JGateway.getTokenBalance(walletAddress, tokenAddress);
            return TokenBalance.builder()
                               .tokenAddress(tokenInfo.getAddress())
                               .rawBalance(tokenBalance.toString())
                               .balance(calculateBalance(tokenBalance, tokenInfo.getDecimals()))
                               .decimals(tokenInfo.getDecimals())
                               .symbol(tokenInfo.getSymbol())
                               .name(tokenInfo.getName())
                               .logo(tokenInfo.getLogo())
                               .type(tokenInfo.getType())
                               .transferable(tokenInfo.isTransferable())
                               .build();
        } catch (Exception e) {
            log.error("Error getting token balances from covalent", e);
            try {
                return getTokenBalances(walletAddress)
                        .stream()
                        .filter(tb -> tb.getTokenAddress().equalsIgnoreCase(tokenAddress))
                        .findFirst().orElse(null);
            } catch (Exception e2) {
                throw ArkaneException.arkaneException()
                                     .message(String.format("Unable to get the balance for the specified account (%s) and token (%s)", walletAddress, tokenAddress))
                                     .errorCode("web3.internal-error")
                                     .build();
            }
        }
    }


    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        CovalentTokenBalanceResponse tokenBalances = covalentClient.getTokenBalances(chainId, walletAddress);
        if (tokenBalances != null && tokenBalances.getData() != null && CollectionUtils.isNotEmpty(tokenBalances.getData().getItems())) {
            return tokenBalances
                    .getData()
                    .getItems()
                    .stream()
                    .filter(i -> !isNativeToken(i))
                    .map(i -> TokenBalance
                            .builder()
                            .tokenAddress(i.getTokenAddress())
                            .rawBalance(i.getBalance().toString())
                            .balance(calculateBalance(i.getBalance(), i.getContractDecimals()))
                            .decimals(i.getContractDecimals())
                            .symbol(i.getSymbol())
                            .name(i.getName())
                            .logo(i.getLogo())
                            .type("ERC20")
                            .transferable(true)
                            .build())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private boolean isNativeToken(CovalentItem item) {
        switch (chainId) {
            case "1":
                return "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee".equalsIgnoreCase(item.getTokenAddress());
            case "56":
                return "BNB".equalsIgnoreCase(item.getSymbol());
            case "80001":
            case "137":
                return "0x0000000000000000000000000000000000001010".equalsIgnoreCase(item.getTokenAddress());
            case "43113":
            case "43114":
                return "0x9debca6ea3af87bf422cea9ac955618ceb56efb4".equalsIgnoreCase(item.getTokenAddress());
            default:
                return false;
        }
    }

    protected double calculateBalance(final BigInteger tokenBalance,
                                      Integer decimals) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
