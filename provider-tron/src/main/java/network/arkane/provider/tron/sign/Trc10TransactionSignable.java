package network.arkane.provider.tron.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
@ToString
public class Trc10TransactionSignable implements Signable {

    private Long amount;
    private String to;
    private String token;

    @Builder
    public Trc10TransactionSignable(final String to,
                                    final Long amount,
                                    final String token) {
        this.to = to;
        this.amount = amount;
        this.token = token;
    }
}
