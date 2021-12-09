package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;

import java.util.List;

@Slf4j
public abstract class BalanceGateway {

    /**
     * Get the native balance for an address
     *
     * @param address
     * @return
     */
    abstract public Balance getBalance(final String address);

    Balance unavailableBalance(final String address) {
        log.debug("Wallet {} could not be searched for type {}", address, type());
        return Balance.builder()
                      .available(false)
                      .secretType(type())
                      .build();
    }

    /**
     * Get the balances for a list of token addresses
     *
     * @param address
     * @param tokenAddresses
     * @return
     */
    public abstract List<TokenBalance> getTokenBalances(String address, List<String> tokenAddresses);

    /**
     * Get the balance of all supported tokens for an address
     *
     * @param address String
     * @return
     */
    public abstract List<TokenBalance> getTokenBalances(String address);

    /**
     * the type this balance gateway supports
     *
     * @return
     */
    public abstract SecretType type();
}
