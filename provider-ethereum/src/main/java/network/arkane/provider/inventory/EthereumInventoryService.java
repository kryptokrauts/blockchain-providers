package network.arkane.provider.inventory;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungable.EthereumNonFungibleGateway;
import org.springframework.stereotype.Component;

@Component
public class EthereumInventoryService extends EvmInventoryService {

    private EthereumNonFungibleGateway ethereumNonFungibleGateway;

    public EthereumInventoryService(EthereumNonFungibleGateway ethereumNonFungibleGateway) {
        super(ethereumNonFungibleGateway);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }
}
