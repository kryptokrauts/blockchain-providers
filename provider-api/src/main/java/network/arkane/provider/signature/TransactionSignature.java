package network.arkane.provider.signature;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionSignature extends Signature {

    protected String signedTransaction;

    @Builder(builderMethodName = "signTransactionBuilder")
    public TransactionSignature(String signedTransaction) {
        this.signedTransaction = signedTransaction;
    }
}
