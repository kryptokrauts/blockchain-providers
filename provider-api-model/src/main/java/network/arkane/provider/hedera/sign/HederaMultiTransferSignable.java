package network.arkane.provider.hedera.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HederaMultiTransferSignable implements Signable {

    private String from;

    private String transactionMemo;

    private List<HederaTransferSignable> transfers;

}
