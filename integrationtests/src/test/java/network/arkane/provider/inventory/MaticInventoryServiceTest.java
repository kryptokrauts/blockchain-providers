package network.arkane.provider.inventory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.blockchainproviders.blockscout.dto.BlockscoutToken;
import network.arkane.provider.business.token.BusinessTokenGateway;
import network.arkane.provider.business.token.model.TokenDto;
import network.arkane.provider.business.token.model.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MaticInventoryServiceTest {

    private BlockscoutClient maticBlockscoutClient;
    private BusinessTokenGateway businessTokenGateway;
    private MaticInventoryService maticInventoryService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        maticBlockscoutClient = mock(BlockscoutClient.class);
        businessTokenGateway = mock(BusinessTokenGateway.class);
        maticInventoryService = new MaticInventoryService(maticBlockscoutClient, businessTokenGateway);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void getInventory() throws IOException {
        String json = "[{\"type\":\"ERC-1155\",\"decimals\":\"\",\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953927655839433583097410118915826251869454338\","
                      + "\"fungible\":false,\"balance\":\"1\"},{\"token_id\":\"57896044618658097711785492504343953927655839433583097410118915826251869454337\","
                      + "\"fungible\":false,\"balance\":\"0\"},{\"token_id\":\"57896044618658097711785492504343953927315557066662158946655541218820101242882\","
                      + "\"fungible\":false,\"balance\":\"0\"},{\"token_id\":\"57896044618658097711785492504343953927315557066662158946655541218820101242881\","
                      + "\"fungible\":false,\"balance\":\"1\"},{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031425\","
                      + "\"fungible\":false,\"balance\":\"3474\"}],\"contractAddress\":\"0x583925da4969ca4674a113248b42825b63fb7cc1\"},{\"type\":\"ERC-1155\",\"decimals\":\"\","
                      + "\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953927315557066662158946655541218820101242881\",\"fungible\":false,\"balance\":\"1\"}],"
                      + "\"contractAddress\":\"0x76bd717df2bb15cfd163380838e21f002b5d5b00\"},{\"type\":\"ERC-1155\",\"decimals\":\"\","
                      + "\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031425\",\"fungible\":false,\"balance\":\"0\"}],"
                      + "\"contractAddress\":\"0x9f7c186ca4ac8346bac3997e1c7225a7c8e8ff0e\"},{\"type\":\"ERC-1155\",\"decimals\":\"\","
                      + "\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953927315557066662158946655541218820101242882\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953927315557066662158946655541218820101242881\",\"fungible\":false,\"balance\":\"0\"}],"
                      + "\"contractAddress\":\"0xb4597fc3b153aba0b54f649f2b1f4e27897fe34b\"},{\"type\":\"ERC-1155\",\"decimals\":\"\","
                      + "\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031425\",\"fungible\":false,\"balance\":\"0\"}],"
                      + "\"contractAddress\":\"0xbf2b8d0ee1cb2f3ddd03db287bc25c92666f27a8\"},{\"type\":\"ERC-20\",\"decimals\":\"18\",\"symbol\":\"TEST\",\"name\":\"Test "
                      + "Token\",\"contractAddress\":\"0xc82c13004c06e4c627cf2518612a55ce7a3db699\"},{\"type\":\"ERC-1155\",\"decimals\":\"\","
                      + "\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031425\",\"fungible\":false,\"balance\":\"1\"}],"
                      + "\"contractAddress\":\"0xd066cb81bebf3836ef9a443ac561f8a4136058d3\"},{\"type\":\"ERC-1155\",\"decimals\":\"\","
                      + "\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031435\",\"fungible\":false,\"balance\":\"200\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031434\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031433\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031432\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031431\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031430\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031429\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031428\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031427\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031426\",\"fungible\":false,\"balance\":\"0\"},"
                      + "{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031425\",\"fungible\":false,\"balance\":\"114268247\"}],"
                      + "\"contractAddress\":\"0xfc73d229dacfde3103fa6abe6cb8d6f7a87e605d\"}]";

        String walletAddress = "0x9c978F4cfa1FE13406BCC05baf26a35716F881Dd";
        List<BlockscoutToken> blockscoutTokens = Arrays.asList(objectMapper.readValue(json, BlockscoutToken[].class));
        when(maticBlockscoutClient.getTokenBalances(walletAddress)).thenReturn(blockscoutTokens);

        when(businessTokenGateway.getToken("0x583925da4969ca4674a113248b42825b63fb7cc1", new BigInteger(
                "57896044618658097711785492504343953927655839433583097410118915826251869454338")))
                .thenReturn(TokenDto.builder().tokenType(TokenType.builder().id(63L).build()).build());
        when(businessTokenGateway.getToken("0x583925da4969ca4674a113248b42825b63fb7cc1", new BigInteger(
                "57896044618658097711785492504343953927315557066662158946655541218820101242881")))
                .thenReturn(TokenDto.builder().tokenType(TokenType.builder().id(62L).build()).build());

        Inventory result = maticInventoryService.getInventory(walletAddress);

        assertThat(result.getContracts()).hasSize(1);
        assertThat(result.getContracts().get(0).getTokenTypes()).hasSize(2);
    }
}
