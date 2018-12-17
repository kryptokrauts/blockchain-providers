package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class VechainTransactionSignableToClause {
    private String to;
    private BigInteger amount;
    private String data;

    @Builder
    public VechainTransactionSignableToClause(String to, BigInteger amount, String data) {
        this.to = to;
        this.amount = amount;
        this.data = data;
    }
}
