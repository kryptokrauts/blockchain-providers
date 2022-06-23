package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigInteger;

@Value
@EqualsAndHashCode(callSuper = true)
public class TokenTransferSignable extends HederaTransferSignable {
    private final BigInteger amount;
    private final String tokenId;

    @Builder
    public TokenTransferSignable(final String spender, final String from, final String to, final BigInteger amount, final String tokenId, final String transactionMemo) {
        super(spender, from, to, transactionMemo);
        this.amount = amount;
        this.tokenId = tokenId;
    }
}


