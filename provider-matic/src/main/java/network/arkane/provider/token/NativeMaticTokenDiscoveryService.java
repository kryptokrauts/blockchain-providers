package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(BlockscoutMaticTokenDiscoveryService.class)
public class NativeMaticTokenDiscoveryService extends NativeEvmTokenDiscoveryService {

    public NativeMaticTokenDiscoveryService(MaticWeb3JGateway maticWeb3JGateway) {
        super(maticWeb3JGateway);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }

    @NotNull
    @Override
    public String getTokenType() {
        return "ERC20";
    }
}
