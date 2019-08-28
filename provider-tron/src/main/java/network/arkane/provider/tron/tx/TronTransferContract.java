package network.arkane.provider.tron.tx;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TronTransferContract implements TronContract {

    private String fromAddress;
    private String toAddress;
    private long amount;

    @Builder
    public TronTransferContract(String fromAddress, String toAddress, long amount) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }
}
