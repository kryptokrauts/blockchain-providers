package network.arkane.provider.avac.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.EvmBalanceGateway;
import network.arkane.provider.balance.EvmBalanceStrategy;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AvacBalanceGateway extends EvmBalanceGateway {

    public AvacBalanceGateway(final List<EvmBalanceStrategy> balanceStrategies) {
        super(balanceStrategies);
    }

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }

}
