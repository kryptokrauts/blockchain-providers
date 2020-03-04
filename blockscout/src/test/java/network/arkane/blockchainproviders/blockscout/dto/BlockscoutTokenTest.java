package network.arkane.blockchainproviders.blockscout.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class BlockscoutTokenTest {

    @Test
    void toDto() throws IOException {
        String response = "[{\"type\":\"ERC-1155\",\"decimals\":\"\",\"tokens\":[{\"token_id\":\"57896044618658097711785492504343953926975274699741220483192166611388333031425\","
                          + "\"fungible\":false,\"balance\":\"1\"},{\"token_id\":\"680564733841876926926749214863536422912\",\"fungible\":true,\"balance\":\"1000\"}],"
                          + "\"contractAddress\":\"0x4354ede79651b3627332faad1a4ceb001d005ce9\"},{\"type\":\"ERC-20\",\"decimals\":\"18\",\"symbol\":\"BNA\",\"name\":\"Banana\","
                          + "\"contractAddress\":\"0xa7ce868f6490186ac57fa12174df770672ec0950\",\"balance\":\"100000000000000000000\"},{\"type\":\"ERC-721\",\"decimals\":\"\","
                          + "\"tokens\":[{\"token_id\":\"1\",\"fungible\":false,\"balance\":\"1\"},{\"token_id\":\"2\",\"fungible\":false,\"balance\":\"1\"},{\"token_id\":\"3\","
                          + "\"fungible\":false,\"balance\":\"1\"}],\"symbol\":\"CPP\",\"name\":\"CryptoPuppies\","
                          + "\"contractAddress\":\"0xf3ceabf330b7bede0745556d71aad08cef4c8e73\"}]";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<BlockscoutToken> tokens = objectMapper.readValue(response, new TypeReference<List<BlockscoutToken>>() {});

        System.out.println(tokens);
    }
}
