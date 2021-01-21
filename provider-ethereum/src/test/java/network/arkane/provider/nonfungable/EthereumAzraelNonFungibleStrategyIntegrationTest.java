package network.arkane.provider.nonfungable;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.contract.EthereumContractService;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class EthereumAzraelNonFungibleStrategyIntegrationTest {

    private EthereumWeb3JGateway ethereumWeb3JGateway;
    private EthereumAzraelNonFungibleStrategy strategy;

    @BeforeEach
    void setUp() {
        ethereumWeb3JGateway = new EthereumWeb3JGateway(Web3j.build(new HttpService("https://rinkeby.arkane.network/")),
                                                        "0x40a38911e470fC088bEEb1a9480c2d69C847BCeC");

        strategy = new EthereumAzraelNonFungibleStrategy(new AzraelClient("https://ethereum-azrael-qa.arkane.network/"),
                                                         new EthereumContractService(ethereumWeb3JGateway),
                                                         Optional.empty());
    }

    @Test
    void getInventory() {
        List<NonFungibleAssetBalance> result = strategy.listNonFungibles("0xA5dC4fb59eBCaf00100C00776b45c3E4b58c6B95");

        assertThat(result).isNotNull();
    }
}
