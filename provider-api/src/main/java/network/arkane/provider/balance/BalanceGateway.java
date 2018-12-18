package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenInfo;

import java.util.List;
import java.util.Optional;

public interface BalanceGateway {

    /**
     * Get the native balance for an address
     * @param address
     * @return
     */
    Balance getBalance(final String address);

    /**
     * Get the balance of a token for an address
     * @param address
     * @param tokenAddress
     * @return
     */
    TokenBalance getTokenBalance(String address, String tokenAddress);

    /**
     * Get the balance of all supported tokens for an address
     * @param address String
     * @return
     */
    List<TokenBalance> getTokenBalances(String address);

    /**
     * the type this balance gateway supports
     * @return
     */
    SecretType type();

    Optional<TokenInfo> getTokenInfo(String tokenAddress);

}
