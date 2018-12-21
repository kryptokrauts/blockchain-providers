package network.arkane.provider;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class PrecisionUtilTest {
    @Test
    void asEth_formDecimalsValue() {
        assertThat(PrecisionUtil.toDecimal(new BigInteger("1000000000000"), 15)).isEqualTo(0.001);
        assertThat(PrecisionUtil.toDecimal(new BigInteger("990000000000000000"), 16)).isEqualTo(99);
        assertThat(PrecisionUtil.toDecimal(new BigInteger("450000000"), 7)).isEqualTo(45);
        assertThat(PrecisionUtil.toDecimal(new BigInteger("45300000000001"), 11)).isEqualTo(453.00000000001);
    }

    @Test
    void asDecimalsValue() {
        assertThat(PrecisionUtil.toRaw(new BigDecimal("0.001"), 15)).isEqualTo(new BigInteger("1000000000000"));
        assertThat(PrecisionUtil.toRaw(new BigDecimal("99.00000000000000001"), 16)).isEqualTo(new BigInteger("990000000000000000"));
        assertThat(PrecisionUtil.toRaw(new BigDecimal("45.00000005"), 7)).isEqualTo(new BigInteger("450000000"));
        assertThat(PrecisionUtil.toRaw(new BigDecimal("453.000000000009"), 11)).isEqualTo(new BigInteger("45300000000001"));
    }
}