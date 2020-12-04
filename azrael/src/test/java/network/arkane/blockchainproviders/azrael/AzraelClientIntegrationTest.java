package network.arkane.blockchainproviders.azrael;

import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.TokenBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class AzraelClientIntegrationTest {

    private AzraelClient client;

    @BeforeEach
    void setUp() {
        client = new AzraelClient("https://matic-azrael-qa.arkane.network");
    }

    @Test
    void getAll() {
        List<TokenBalance> result = client.getTokens("0x9c978F4cfa1FE13406BCC05baf26a35716F881Dd");

        assertThat(result).isNotEmpty();
    }

    @Test
    void getByContractTypes() {
        List<TokenBalance> result = client.getTokens("0x9c978F4cfa1FE13406BCC05baf26a35716F881Dd", singletonList(ContractType.ERC_20));

        assertThat(result).isNotEmpty();
        assertThat(result).extracting("type").containsOnly(ContractType.ERC_20);

    }
}
