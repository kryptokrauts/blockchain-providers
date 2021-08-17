package network.arkane.provider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class PrecisionUtil {

    public static double toDecimal(final BigInteger rawValue,
                                   final int precision) {
        return new BigDecimal(rawValue).divide(BigDecimal.TEN.pow(precision), precision, RoundingMode.HALF_DOWN).doubleValue();
    }

    public static double toDecimal(final Long rawValue,
                                   final int precision) {
        return toDecimal(BigInteger.valueOf(rawValue), precision);
    }

    public static BigInteger toRaw(final BigDecimal decimalValue,
                                   final int precision) {
        return decimalValue.multiply(BigDecimal.TEN.pow(precision)).setScale(0, RoundingMode.HALF_DOWN).toBigInteger();
    }
}
