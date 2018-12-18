package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.util.List;

@Data
@NoArgsConstructor
public class VechainTransactionSignable implements Signable {

    private String chainTag;
    private String blockRef;
    private int expiration;

    private List<VechainTransactionSignableToClause> clauses;
    private Integer gasPriceCoef = 0;
    private int gas;
    private String nonce;

    @Builder
    public VechainTransactionSignable(final String chainTag,
                                      final String blockRef,
                                      final int expiration,
                                      final List<VechainTransactionSignableToClause> clauses,
                                      final Integer gasPriceCoef,
                                      final int gas,
                                      final String nonce) {
        this.chainTag = chainTag;
        this.blockRef = blockRef;
        this.expiration = expiration;
        this.clauses = clauses;
        this.gasPriceCoef = gasPriceCoef;
        this.gas = gas;
        this.nonce = nonce;
    }
}
