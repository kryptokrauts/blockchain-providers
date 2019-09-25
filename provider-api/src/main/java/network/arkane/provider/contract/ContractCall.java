package network.arkane.provider.contract;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContractCall {
    private String contractAddress;
    private String functionName;
    private String caller;
    private List<ContractCallParam> inputs;
    private List<ContractCallResultParam> outputs;


}
