package network.arkane.provider.tron.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
@ToString
public class TrxTransactionSignable implements Signable {

    private Long amount;
    private String to;
    private String data;

    @Builder
    public TrxTransactionSignable(final String to,
                                  final Long amount,
                                  final String data) {
        this.to = to;
        this.amount = amount;
        this.data = data;
    }
}
