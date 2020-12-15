package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;

import java.util.List;

public interface EvmBalanceStrategy {
    Balance getBalance(String account);

    TokenBalance getTokenBalance(final String owner,
                                 final String tokenAddress);


    List<TokenBalance> getTokenBalances(String walletAddress);

    SecretType type();
}
