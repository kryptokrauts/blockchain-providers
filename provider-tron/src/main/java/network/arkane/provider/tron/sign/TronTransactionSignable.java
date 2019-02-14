package network.arkane.provider.tron.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
@ToString
public class TronTransactionSignable implements Signable {

    private Long amount;
    private String to;

    @Builder
    public TronTransactionSignable(final String to,
                                   final Long amount) {
        this.to = to;
        this.amount = amount;
    }
}
