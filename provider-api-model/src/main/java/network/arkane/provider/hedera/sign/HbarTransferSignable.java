package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigInteger;

@Value
@EqualsAndHashCode(callSuper = true)
public class HbarTransferSignable extends HederaTransferSignable {
    private final BigInteger amount;

    @Builder
    public HbarTransferSignable(final String spender, final String from, final String to, final BigInteger amount, final String transactionMemo) {
        super(spender, from, to, transactionMemo);
        this.amount = amount;
    }
}


