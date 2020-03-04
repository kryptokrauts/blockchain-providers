package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.TokenBalance;

import java.math.BigInteger;
import java.util.List;

public interface MaticBalanceStrategy {
    BigInteger getBalance(String account);

    BigInteger getTokenBalance(final String owner,
                               final String tokenAddress);


    List<TokenBalance> getTokenBalances(String walletAddress);
}
