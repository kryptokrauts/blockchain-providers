package network.arkane.provider.avac.inventory;

import network.arkane.provider.avac.nonfungible.AvacNonFungibleGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.inventory.EvmInventoryService;
import org.springframework.stereotype.Component;

@Component
public class AvacInventoryService extends EvmInventoryService {

    private AvacNonFungibleGateway avacNonFungibleGateway;

    public AvacInventoryService(AvacNonFungibleGateway avacNonFungibleGateway) {
        super(avacNonFungibleGateway);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.AVAC;
    }
}
