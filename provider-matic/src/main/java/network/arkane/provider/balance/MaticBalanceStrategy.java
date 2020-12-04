package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;

import java.util.List;

public interface MaticBalanceStrategy {
    Balance getBalance(String account);

    TokenBalance getTokenBalance(final String owner,
                                 final String tokenAddress);


    List<TokenBalance> getTokenBalances(String walletAddress);
}
