package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;

import java.util.List;

public class AeternityBalanceGateway implements BalanceGateway {

    @Override
    public Balance getBalance(String address) {
        // TODO
        return null;
    }

    @Override
    public TokenBalance getTokenBalance(String address, String tokenAddress) {
        // TODO
        return null;
    }

    @Override
    public List<TokenBalance> getTokenBalances(String address) {
        // TODO
        return null;
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }
}
