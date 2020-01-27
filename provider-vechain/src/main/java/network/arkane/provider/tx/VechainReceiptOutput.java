package network.arkane.provider.tx;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VechainReceiptOutput {

    private String contractAddress;
    private List<VechainTxTransfer> transfers;
    private List<VechainTxEvent> events;

}
