package network.arkane.provider.sign.domain;

public class TransactionSignatureMother {
    public static TransactionSignature aSignTransactionResponse() {
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction("transaction")
                .build();
    }
}