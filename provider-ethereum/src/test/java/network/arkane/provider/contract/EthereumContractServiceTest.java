package network.arkane.provider.contract;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class EthereumContractServiceTest {

    private EthereumContractService ethereumContractService;

    @BeforeEach
    void setUp() {
        ethereumContractService = new EthereumContractService(new EthereumWeb3JGateway(Web3j.build(new HttpService("https://parity.arkane.network")),
                                                                                       "0x06012c8cf97bead5deae237070f9587f8e7a266d"));
    }

    @Test
    void cryptokittiesCall() {
        List<String> result = ethereumContractService.callFunction(ContractCall.builder()
                                                                               .contractAddress("0x06012c8cf97bead5deae237070f9587f8e7a266d")
                                                                               .functionName("isPregnant")
                                                                               .inputs(Collections.singletonList(ContractCallParam.builder().type("uint256").value("1698495").build()))
                                                                               .outputs(Collections.singletonList(ContractCallResultParam.builder().type("bool").build()))
                                                                               .build());

        assertThat(result.get(0)).isEqualTo("false");
    }
}