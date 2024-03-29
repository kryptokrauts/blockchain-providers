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

    /**
     * @return a Balance object with balance of 0
     */
    abstract public Balance getZeroBalance();

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
     * @param walletAddress
     * @param tokenAddresses
     * @return
     */
    public abstract List<TokenBalance> getTokenBalances(String walletAddress, List<String> tokenAddresses);

    /**
     * Get the balance of all supported tokens for an address
     *
     * @param walletAddress String
     * @return
     */
    public abstract List<TokenBalance> getTokenBalances(String walletAddress);

    /**
     * the type this balance gateway supports
     *
     * @return
     */
    public abstract SecretType type();
}
