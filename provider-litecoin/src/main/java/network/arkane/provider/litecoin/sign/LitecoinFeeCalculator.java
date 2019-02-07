package network.arkane.provider.litecoin.sign;

import org.bitcoinj.core.Transaction;
import org.springframework.stereotype.Component;

@Component
public class LitecoinFeeCalculator {

    private final LitecoinTransactionSizeEstimator estimator;

    public LitecoinFeeCalculator(LitecoinTransactionSizeEstimator estimator) {
        this.estimator = estimator;
    }

    public Long calculate(Transaction transaction, int feePerKiloByte) {
        int estimateFinalSize = estimator.estimateFinalSize(transaction);

        return (long) Math.ceil(estimateFinalSize / 1000.0) * feePerKiloByte;
    }
}
