package network.arkane.provider.signature;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubmittedAndSignedTransactionSignature extends TransactionSignature {

    private String transactionHash;

    @Builder(builderMethodName = "signAndSubmitTransactionBuilder")
    public SubmittedAndSignedTransactionSignature(final String transactionHash,
                                                  final String signedTransaction) {
        super(signedTransaction);
        this.transactionHash = transactionHash;
    }
}
