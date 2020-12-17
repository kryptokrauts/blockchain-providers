package network.arkane.provider.neo.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.neo.NeoW3JConfiguration;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Disabled
class NeoBalanceGatewayIntegrationTest {

    @Test
    void getBalance() {
        NeoBalanceGateway balanceGateway = new NeoBalanceGateway(new NeoW3JGateway(new NeoW3JConfiguration().neoNeow3j("https://neo-testnet.arkane.network")), null);
        Balance result = balanceGateway.getBalance("AKJrLM5QCdr8opd1JZhYWnHZrBJbz6rbXv");

        assertThat(result).isNotNull();
    }
}
