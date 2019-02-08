package network.arkane.provider.litecoin.sign;


import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import org.bitcoinj.core.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LitecoinFeeCalculatorTest {

    private LitecoinTransactionSizeEstimator estimator;
    private LitecoinFeeCalculator calculator;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        estimator = mock(LitecoinTransactionSizeEstimator.class);
        calculator = new LitecoinFeeCalculator(estimator);
        transaction = new Transaction(new LitecoinParams());
    }

    @Test
    void calculatesFee() {
        when(estimator.estimateFinalSize(transaction)).thenReturn(372);

        Long fee = calculator.calculate(transaction, 99817);

        assertThat(fee).isEqualTo(37132);
    }


}