package network.arkane.provider.contract;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.clients.DeltaBalancesContractClient;
import network.arkane.provider.core.model.blockchain.NodeProvider;
import network.arkane.provider.gateway.VechainGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Disabled
class VechainContractServiceTest {


    private VechainContractService vechainContractService;

    @BeforeEach
    void setUp() {
        NodeProvider nodeProvider = NodeProvider.getNodeProvider();
        nodeProvider.setProvider("https://thor-test.arkane.network");
        nodeProvider.setTimeout(10000);
        DeltaBalancesContractClient deltaBalancesContractClient = new DeltaBalancesContractClient("0xd8a100bccb8cb27ad7ef64d24e30949497e486aa");
        vechainContractService = new VechainContractService(new VechainGateway(null));
    }

    @Test
    void vthoBalanceCall() {
        List<Object> result = vechainContractService.callFunction(ContractCall.builder()
                                                                              .contractAddress("0x0000000000000000000000000000456E65726779")
                                                                              .functionName("balanceOf")
                                                                              .inputs(Collections.singletonList(ContractCallParam.builder()
                                                                                                                                 .type("address")
                                                                                                                                 .value("0xB8C73790E4FBFdC3e879aF1B43fAdC2a9d43A1E1")
                                                                                                                                 .build()))
                                                                              .outputs(Collections.singletonList(ContractCallResultParam.builder().type("uint256").build()))
                                                                              .build());

        assertThat(result.get(0)).isNotNull();
        log.debug(String.valueOf(result.get(0)));
    }

}