package network.arkane.provider.litecoin.sign;

import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import org.bitcoinj.core.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


class LitecoinTransactionSizeEstimatorTest {

    TransactionOutput transactionOutput = mock(TransactionOutput.class);
    TransactionInput transactionInput = mock(TransactionInput.class);

    LitecoinTransactionSizeEstimator estimator = new LitecoinTransactionSizeEstimator();

    @Test
    void estimateNoInputsAndOutputs() {
        Transaction transaction = new Transaction(new LitecoinParams());

        int bytes = estimator.estimateFinalSize(transaction);

        assertThat(bytes).isEqualTo(44);
    }

    @Test
    void estimateOneInput() {
        Transaction transaction = new Transaction(new LitecoinParams());
        transaction.addInput(transactionInput);

        int bytes = estimator.estimateFinalSize(transaction);

        assertThat(bytes).isEqualTo(191);
    }

    @Test
    void estimateOneOutput() {
        Transaction transaction = new Transaction(new LitecoinParams());
        transaction.addOutput(transactionOutput);

        int bytes = estimator.estimateFinalSize(transaction);

        assertThat(bytes).isEqualTo(78);
    }

    @Test
    void estimateOneInputTwoOutputs() {
        Transaction transaction = new Transaction(new LitecoinParams());
        transaction.addInput(transactionInput);
        transaction.addOutput(transactionOutput);
        transaction.addOutput(transactionOutput);

        int bytes = estimator.estimateFinalSize(transaction);

        assertThat(bytes).isEqualTo(259);
    }

    @Test
    void estimateTwoInputsTwoOutputs() {
        Transaction transaction = new Transaction(new LitecoinParams());
        transaction.addInput(transactionInput);
        transaction.addInput(transactionInput);
        transaction.addOutput(transactionOutput);
        transaction.addOutput(transactionOutput);

        int bytes = estimator.estimateFinalSize(transaction);

        assertThat(bytes).isEqualTo(406);
    }
}