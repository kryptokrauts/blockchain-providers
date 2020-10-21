package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.GochainWeb3JGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NativeGochainTokenDiscoveryService extends NativeEvmTokenDiscoveryService {

    public NativeGochainTokenDiscoveryService(GochainWeb3JGateway web3JGateway) {
        super(web3JGateway);
    }

    @NotNull
    @Override
    public String getTokenType() {
        return "GO20";
    }

    @Override
    public SecretType type() {
        return SecretType.GOCHAIN;
    }


}
