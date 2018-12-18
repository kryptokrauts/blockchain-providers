package network.arkane.provider.sign;

import network.arkane.provider.sign.domain.TransactionSignature;

public class TransactionSignatureMother {
    public static TransactionSignature aSignTransactionResponse() {
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction("transaction")
                .build();
    }
}