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
    void calculatesFee_lessThen1KB() {
        when(estimator.estimateFinalSize(transaction)).thenReturn(999);

        Long fee = calculator.calculate(transaction, 100000);

        assertThat(fee).isEqualTo(100000);
    }


    @Test
    void calculatesFee_1KB() {
        when(estimator.estimateFinalSize(transaction)).thenReturn(1000);

        Long fee = calculator.calculate(transaction, 100000);

        assertThat(fee).isEqualTo(100000);
    }

    @Test
    void calculatesFee_moreThen1KB() {
        when(estimator.estimateFinalSize(transaction)).thenReturn(1001);

        Long fee = calculator.calculate(transaction, 100000);

        assertThat(fee).isEqualTo(200000);
    }

    @Test
    void calculatesFee_almost2KB() {
        when(estimator.estimateFinalSize(transaction)).thenReturn(1999);

        Long fee = calculator.calculate(transaction, 100000);

        assertThat(fee).isEqualTo(200000);
    }


}