package network.arkane.provider.inventory;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.MaticNonFungibleGateway;
import org.springframework.stereotype.Component;

@Component
public class MaticInventoryService extends EvmInventoryService {

    private MaticNonFungibleGateway maticNonFungibleGateway;

    public MaticInventoryService(MaticNonFungibleGateway maticNonFungibleGateway) {
        super(maticNonFungibleGateway);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }
}
