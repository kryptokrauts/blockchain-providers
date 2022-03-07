package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@Builder
public class HbarTransferSignable implements HederaTransferSignable {
    private String from;
    private String to;
    private BigInteger amount;
    private String transactionMemo;
}


