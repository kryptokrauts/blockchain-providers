package network.arkane.blockchainproviders.blockscout;

import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.blockchainproviders.blockscout.dto.BlockscoutToken;
import network.arkane.provider.token.TokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

@Disabled
class BlockscoutClientIntegrationTest {

    private BlockscoutClient client;

    @BeforeEach
    void setUp() {
        client = new BlockscoutClient("https://blockscout.com/eth/mainnet/api", new ObjectMapper());
    }

    @Test
    void getBalance() {
        BigInteger balance = client.getBalance("0xf2bfC7111B09807Ce9ab9B84dE9926d23d5E21B1");

        System.out.println(balance);
    }

    @Test
    void getTokenBalance() {
        BigInteger balance = client.getTokenBalance("0x1299d8d73A7E726933679eeb68a2cdcEa9717CE1", "0x4DF47B4969B2911C966506E3592c41389493953b");

        System.out.println(balance);
    }

    @Test
    void getTokenInfo() {
        TokenInfo tokenInfo = client.getTokenInfo("0x4DF47B4969B2911C966506E3592c41389493953b").get();

        System.out.println(tokenInfo);
    }

    @Test
    void getTokenBalances() {
        List<BlockscoutToken> result = client.getTokenBalances("0x4DF47B4969B2911C966506E3592c41389493953b");

        System.out.println(result);
    }

}
