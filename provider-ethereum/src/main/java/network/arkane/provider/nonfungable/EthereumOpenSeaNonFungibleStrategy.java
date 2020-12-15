package network.arkane.provider.nonfungable;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.nonfungible.OpenseaNonFungibleStrategy;
import network.arkane.provider.opensea.OpenSeaGateway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ethereum.nonfungible.strategy", havingValue = "opensea")
public class EthereumOpenSeaNonFungibleStrategy extends OpenseaNonFungibleStrategy {

    public EthereumOpenSeaNonFungibleStrategy(EthereumWeb3JGateway ethereumWeb3JGateway,
                                              OpenSeaGateway ethereumOpenSeaGateway) {
        super(ethereumWeb3JGateway, ethereumOpenSeaGateway);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }
}
