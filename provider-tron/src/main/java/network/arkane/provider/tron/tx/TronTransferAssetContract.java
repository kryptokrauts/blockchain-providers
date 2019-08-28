package network.arkane.provider.tron.tx;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TronTransferAssetContract extends TronTransferContract {

    private String asset;

    @Builder(builderMethodName = "transferAssetContractBuilder")
    public TronTransferAssetContract(String fromAddress, String toAddress, long amount, String asset) {
        super(fromAddress, toAddress, amount);
        this.asset = asset;
    }
}
