package network.arkane.provider.nonfungible;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.contract.MaticContractService;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class MaticNonFungibleGatewayIntegrationTest {


    private MaticWeb3JGateway maticWeb3JGateway;
    private MaticAzraelNonFungibleStrategy strategy;

    @BeforeEach
    void setUp() {
        maticWeb3JGateway = new MaticWeb3JGateway(Web3j.build(new HttpService("https://matic-mumbai.arkane.network/")),
                                                  "0x40a38911e470fC088bEEb1a9480c2d69C847BCeC");

        strategy = new MaticAzraelNonFungibleStrategy(new AzraelClient("https://matic-azrael-qa.arkane.network/"), new MaticContractService(maticWeb3JGateway), Optional.empty());
    }

    @Test
    void getSome() {
        List<NonFungibleAsset> nonFungibleAssets = strategy.listNonFungibles("0x9c978F4cfa1FE13406BCC05baf26a35716F881Dd");

        assertThat(nonFungibleAssets).isNotNull();
    }
}
