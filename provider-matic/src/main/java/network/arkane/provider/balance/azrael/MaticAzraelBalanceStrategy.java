package network.arkane.provider.balance.azrael;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.MaticBalanceStrategy;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name = "matic.balance.strategy", havingValue = "azrael")
public class MaticAzraelBalanceStrategy implements MaticBalanceStrategy {

    private final MaticAzraelBalanceGateway azraelBalanceGateway;

    public MaticAzraelBalanceStrategy(MaticAzraelBalanceGateway azraelBalanceGateway) {
        this.azraelBalanceGateway = azraelBalanceGateway;
    }

    @Override
    public Balance getBalance(String account) {
        return azraelBalanceGateway.getBalance(account);
    }

    @Override
    public TokenBalance getTokenBalance(String owner,
                                        String tokenAddress) {
        return azraelBalanceGateway.getTokenBalance(owner, tokenAddress);
    }

    @Override
    public List<TokenBalance> getTokenBalances(String walletAddress) {
        return azraelBalanceGateway.getTokenBalances(walletAddress);
    }
}
