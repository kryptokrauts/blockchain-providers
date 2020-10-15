package network.arkane.provider.bsc.token;

import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.NativeEvmTokenDiscoveryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NativeBscTokenDiscoveryService extends NativeEvmTokenDiscoveryService {

    public NativeBscTokenDiscoveryService(BscWeb3JGateway web3JGateway) {
        super(web3JGateway);
    }

    @NotNull
    @Override
    public String getTokenType() {
        return "BEP20";
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }


}
