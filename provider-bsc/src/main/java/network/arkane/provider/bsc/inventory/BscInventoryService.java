package network.arkane.provider.bsc.inventory;

import network.arkane.provider.bsc.nonfungible.BscNonFungibleGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.inventory.EvmInventoryService;
import org.springframework.stereotype.Component;

@Component
public class BscInventoryService extends EvmInventoryService {

    private BscNonFungibleGateway bscNonFungibleGateway;

    public BscInventoryService(BscNonFungibleGateway bscNonFungibleGateway) {
        super(bscNonFungibleGateway);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.BSC;
    }
}
