package network.arkane.provider.litecoin.sign;

import org.bitcoinj.core.Transaction;
import org.springframework.stereotype.Component;

@Component
public class LitecoinTransactionSizeEstimator {

    public int estimateFinalSize(Transaction transaction) {
        int numberOfInputs = transaction.getInputs().size();
        int numberOfOutputs = addFutureOutput(transaction.getOutputs().size());

        return (numberOfInputs * 148) + (numberOfOutputs * 34) + 10 - numberOfInputs;
    }

    private int addFutureOutput(int size) {
        return size + 1;
    }
}
