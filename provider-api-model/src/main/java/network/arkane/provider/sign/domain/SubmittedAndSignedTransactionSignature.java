package network.arkane.provider.sign.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubmittedAndSignedTransactionSignature extends TransactionSignature {

    private String transactionHash;

    private Object transactionDetails;

    public SubmittedAndSignedTransactionSignature(final String transactionHash,
                                                  final String signedTransaction) {
        super(signedTransaction, SignatureType.SUBMITTED_AND_SIGNED_TRANSACTION_SIGNATURE);
        this.transactionHash = transactionHash;
    }

    @Builder(builderMethodName = "signAndSubmitTransactionBuilder")
    public SubmittedAndSignedTransactionSignature(final String transactionHash,
                                                  final String signedTransaction,
                                                  final Object transactionDetails) {
        super(signedTransaction, SignatureType.SUBMITTED_AND_SIGNED_TRANSACTION_SIGNATURE);
        this.transactionHash = transactionHash;
        this.transactionDetails = transactionDetails;
    }
}
