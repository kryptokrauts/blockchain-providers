package network.arkane.provider.signature;

public class TransactionSignatureMother {
    public static TransactionSignature aSignTransactionResponse() {
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction("transaction")
                .build();
    }
}