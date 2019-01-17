package network.arkane.provider.sign.domain;

import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

class SubmittedAndSignedTransactionSignatureMother {
    public static SubmittedAndSignedTransactionSignature aSignAndSubmitEthereumTransactionResponse() {
        return signAndSubmitTransactionBuilder()
                .signedTransaction("transaction")
                .transactionHash("txHash")
                .build();
    }
}