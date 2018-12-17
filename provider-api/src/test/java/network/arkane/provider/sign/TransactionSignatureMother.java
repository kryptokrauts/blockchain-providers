package network.arkane.provider.sign;

public class TransactionSignatureMother {
    public static TransactionSignature aSignTransactionResponse() {
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction("transaction")
                .build();
    }
}