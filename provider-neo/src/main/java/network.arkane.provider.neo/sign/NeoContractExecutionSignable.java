package network.arkane.provider.neo.sign;

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
    private List<NeoContractParameter> inputs;
    private List<NeoAssetTransferSignable> outputs;

    @Builder
    public NeoContractExecutionSignable(String contractScriptHash,
                                        String functionName,
                                        String networkFee,
                                        List<NeoContractParameter> inputs,
                                        List<NeoAssetTransferSignable> outputs) {
        this.contractScriptHash = contractScriptHash;
        this.functionName = functionName;
        this.networkFee = networkFee;
        this.inputs = inputs;
        this.outputs = outputs;
    }

}
