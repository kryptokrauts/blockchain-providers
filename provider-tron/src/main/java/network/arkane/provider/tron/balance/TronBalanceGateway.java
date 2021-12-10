package network.arkane.provider.tron.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ChainNoLongerSupportedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TronBalanceGateway extends BalanceGateway {

    @Override
    public SecretType type() {
        return SecretType.TRON;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "tron-node")
    public Balance getBalance(final String account) {
        throw new ChainNoLongerSupportedException();
    }

    @Override
    public Balance getZeroBalance() {
        throw new ChainNoLongerSupportedException();
    }

    @Override
    public List<TokenBalance> getTokenBalances(String address,
                                               List<String> tokenAddresses) {
        throw new ChainNoLongerSupportedException();

    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        throw new ChainNoLongerSupportedException();
    }
}
