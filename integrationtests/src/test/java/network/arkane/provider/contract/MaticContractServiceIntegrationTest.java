package network.arkane.provider.contract;

import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.Collections;
import java.util.List;

@Disabled
class MaticContractServiceIntegrationTest {

    private MaticContractService maticContractService;

    @BeforeEach
    void setUp() {
        maticContractService = new MaticContractService(new MaticWeb3JGateway(Web3j.build(new HttpService("https://matic-mumbai.arkane.network")),
                                                                              "0x40a38911e470fC088bEEb1a9480c2d69C847BCeC"));
    }

    @Test
    void blah() {
        ContractCall call = ContractCall.builder()
                                        .contractAddress("0xfc73d229dacfde3103fa6abe6cb8d6f7a87e605d")
                                        .functionName("tokenUri")
                                        .inputs(Collections.emptyList())
                                        .outputs(Collections.singletonList(ContractCallResultParam.builder().type("string").build()))
                                        .build();

        List<ContractCallResultType> result = maticContractService.callFunction(call);

        System.out.println(result);
    }
}
