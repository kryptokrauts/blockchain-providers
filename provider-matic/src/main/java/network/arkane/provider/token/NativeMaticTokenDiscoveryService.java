package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NativeMaticTokenDiscoveryService extends NativeEvmTokenDiscoveryService {

    public NativeMaticTokenDiscoveryService(MaticWeb3JGateway maticWeb3JGateway) {
        super(maticWeb3JGateway);
    }

    @NotNull
    @Override
    public String getTokenType() {
        return "ERC20";
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }


}
