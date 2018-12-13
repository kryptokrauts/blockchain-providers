package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;

import java.util.List;

public interface BalanceChecker {

    SecretType getChain();

    Balance getBalance(final String wallet);

    TokenBalance getTokenBalance(String address, String tokenAddress);

    List<TokenBalance> getTokenBalances(String address);
}
