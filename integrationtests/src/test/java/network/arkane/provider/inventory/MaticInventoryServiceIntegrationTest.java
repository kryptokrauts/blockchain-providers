package network.arkane.provider.inventory;

import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.business.infrastructure.BusinessClientImpl;
import network.arkane.provider.business.token.BusinessTokenGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class MaticInventoryServiceIntegrationTest {

    private MaticInventoryService inventoryService;

    @BeforeEach
    void setUp() {

        inventoryService = new MaticInventoryService(new BlockscoutClient("https://explorer-testnet2-matic.arkane.network/api"),
                                                     new BusinessTokenGateway(new BusinessClientImpl("https", "api-business-qa.arkane.network")));
    }

    @Test
    void getInventory() {
        Inventory inventory = inventoryService.getInventory("0x9c978F4cfa1FE13406BCC05baf26a35716F881Dd");
        System.out.println(inventory);
    }
}
