package network.arkane.provider.inventory;

import network.arkane.provider.chain.SecretType;

public interface InventoryService {

    SecretType getSecretType();

    Inventory getInventory(final String walletAddress,
                           final String... contractAddresses);


}
