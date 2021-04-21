package network.arkane.provider.avac.token;

import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.NativeEvmTokenDiscoveryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NativeAvacTokenDiscoveryService extends NativeEvmTokenDiscoveryService {

    public NativeAvacTokenDiscoveryService(AvacWeb3JGateway web3JGateway) {
        super(web3JGateway);
    }

    @NotNull
    @Override
    public String getTokenType() {
        return "ERC20";
    }

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }


}
