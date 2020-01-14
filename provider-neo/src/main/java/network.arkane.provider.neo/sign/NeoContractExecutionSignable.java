package network.arkane.provider.neo.sign;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.util.List;

@Data
@NoArgsConstructor
public class NeoContractExecutionSignable implements Signable {

    private String contractScriptHash;
    private String functionName;
    private String networkFee;
    private String systemFee;
    private List<JsonNode> inputs;
    private List<NeoAssetTransferSignable> outputs;

    @Builder
    public NeoContractExecutionSignable(String contractScriptHash,
                                        String functionName,
                                        String networkFee,
                                        String systemFee,
                                        List<JsonNode> inputs,
                                        List<NeoAssetTransferSignable> outputs) {
        this.contractScriptHash = contractScriptHash;
        this.functionName = functionName;
        this.networkFee = networkFee;
        this.systemFee = systemFee;
        this.inputs = inputs;
        this.outputs = outputs;
    }

}
