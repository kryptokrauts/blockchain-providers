package network.arkane.provider.core.model.clients;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.BytesUtils;
import network.arkane.provider.Prefix;
import network.arkane.provider.core.model.clients.base.AbstractToken;
import network.arkane.provider.core.model.exception.ClientArgumentException;
import network.arkane.provider.utils.BlockchainUtils;
import network.arkane.provider.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Amount for {@link ToClause} to use.
 */
@Slf4j
public class Amount {
    /**
     * If you need send 0 amount, the use {@link Amount#ZERO}
     */
    public static final Amount ZERO = new Zero();

    private AbstractToken abstractToken;
    private BigDecimal amount;

    /**
     * Create {@link Amount} from abstractToken
     *
     * @param token {@link AbstractToken}
     * @return {@link Amount} object
     */
    public static Amount createFromToken(AbstractToken token) {
        Amount amount = new Amount();
        amount.abstractToken = token;
        return amount;
    }

    /**
     * Create a VET amount
     *
     * @return {@link Amount}
     */
    public static Amount VET() {
        return Amount.createFromToken(AbstractToken.VET);
    }

    /**
     * Create a VTHO amount
     *
     * @return {@link Amount}
     */
    public static Amount VTHO() {
        return Amount.createFromToken(ERC20Token.VTHO);
    }

    private Amount() {
    }

    /**
     * Set hex string to abstractToken value.
     *
     * @param hexAmount hex amount with "0x", if it is 0, use {@link Amount#ZERO} constant
     *                  instance.
     */
    public Amount setHexAmount(String hexAmount) {
        String noPrefixAmount = StringUtils.sanitizeHex(hexAmount);
        if (StringUtils.isBlank(noPrefixAmount)) {
            return this;
        }
        if (!StringUtils.isHex(hexAmount)) {
            log.error("{} is not a hex amount", hexAmount);
            throw ClientArgumentException.exception("setHexValue argument hex value.");
        }
        amount = BlockchainUtils.amount(noPrefixAmount, abstractToken.getPrecision().intValue(),
                                        abstractToken.getScale().intValue());
        return this;
    }

    /**
     * Set decimal amount string
     *
     * @param decimalAmount decimal amount string.
     */
    public Amount setDecimalAmount(String decimalAmount) {
        if (StringUtils.isBlank(decimalAmount)) {
            throw new IllegalArgumentException("Decimal amount string is blank");
        }
        amount = new BigDecimal(decimalAmount);
        return this;
    }

    public Amount setBigIntegerAmount(final BigInteger bigIntegerAmount) {
        amount = new BigDecimal(bigIntegerAmount).divide(BigDecimal.TEN.pow(abstractToken.getPrecision().intValue()),
                                                         abstractToken.getPrecision().intValue(),
                                                         RoundingMode.HALF_DOWN);
        return this;
    }

    public String toHexString() {
        BigDecimal fullDecimal = amount.multiply(BigDecimal.TEN.pow(abstractToken.getPrecision().intValue()));
        byte[] bytes = BytesUtils.trimLeadingZeroes(fullDecimal.toBigInteger().toByteArray());
        return BytesUtils.toHexString(bytes, Prefix.ZeroLowerX);
    }

    public BigInteger toBigInteger() {
        BigDecimal fullDecimal = amount.multiply(BigDecimal.TEN.pow(abstractToken.getPrecision().intValue()));
        return fullDecimal.toBigInteger();
    }

    public AbstractToken getAbstractToken() {
        return abstractToken;
    }

    /**
     * Get amount
     *
     * @return {@link BigDecimal} value.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Convert to byte array.
     *
     * @return byte[]
     */
    public byte[] toByteArray() {
        return BlockchainUtils.byteArrayAmount(amount, abstractToken.getPrecision().intValue());
    }

    private static class Zero extends Amount {
        public byte[] toByteArray() {
            return new byte[] {};
        }

        public BigDecimal getAmount() {
            return new BigDecimal(0);
        }
    }
}
