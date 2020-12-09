package network.arkane.provider.nonfungable;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.EvmNonFungibleGateway;
import network.arkane.provider.nonfungible.EvmNonFungibleStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EthereumNonFungibleGateway extends EvmNonFungibleGateway {


    public EthereumNonFungibleGateway(List<EvmNonFungibleStrategy> strategies) {
        super(strategies);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }

}
