package network.arkane.blockchainproviders.covalent;

import network.arkane.blockchainproviders.covalent.dto.CovalentTokenBalanceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class CovalentClientIntegrationTest {

    private CovalentClient client;

    @BeforeEach
    void setUp() {
        client = new CovalentClient("https://api.covalenthq.com/v1/", "");
    }

    @Test
    void getTokens() {
        CovalentTokenBalanceResponse result = client.getTokenBalances("1", "0x1299d8d73A7E726933679eeb68a2cdcEa9717CE1");

        assertThat(result).isNotNull();
    }
}
