package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MaticBalanceGateway extends EvmBalanceGateway {


    public MaticBalanceGateway(List<EvmBalanceStrategy> evmBalanceStrategies) {
        super(evmBalanceStrategies);
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }

}
