package network.arkane.provider.nonfungable;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class NonFungibleContractTypeMapperTest {


    private NonFungibleContractTypeMapper contractTypeMapper;

    @BeforeEach
    void setUp() {
        contractTypeMapper = new NonFungibleContractTypeMapper(new EthereumWeb3JGateway(Web3j.build(new HttpService("https://ethereum.arkane.network")),
                                                                                        "0x40a38911e470fC088bEEb1a9480c2d69C847BCeC"));
    }

    @Test
    void erc1155Contract() {
        String type = contractTypeMapper.getType("0xfaaFDc07907ff5120a76b34b731b278c38d6043C");

        assertThat(type).isEqualTo("ERC_1155");
    }

    @Test
    void erc721Contract() {
        String type = contractTypeMapper.getType("0x06012c8cf97bead5deae237070f9587f8e7a266d");

        assertThat(type).isEqualTo("ERC_721");
    }
}
