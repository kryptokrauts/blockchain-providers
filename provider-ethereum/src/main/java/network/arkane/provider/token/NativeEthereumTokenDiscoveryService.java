package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NativeEthereumTokenDiscoveryService extends NativeEvmTokenDiscoveryService {

    public NativeEthereumTokenDiscoveryService(EthereumWeb3JGateway ethereumWeb3JGateway) {
        super(ethereumWeb3JGateway);
    }

    @NotNull
    @Override
    public String getTokenType() {
        return "ERC20";
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }


}
