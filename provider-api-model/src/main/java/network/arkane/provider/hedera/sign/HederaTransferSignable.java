package network.arkane.provider.hedera.sign;

import lombok.Value;
import lombok.experimental.NonFinal;
import network.arkane.provider.sign.domain.Signable;

@Value
@NonFinal
public abstract class HederaTransferSignable implements Signable {
    private final String spender;
    private final String from;
    private final String to;
    private final String transactionMemo;
}
