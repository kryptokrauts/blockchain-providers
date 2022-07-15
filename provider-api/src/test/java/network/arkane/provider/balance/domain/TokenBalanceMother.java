package network.arkane.provider.balance.domain;

import network.arkane.provider.token.TokenInfo;
import network.arkane.provider.token.TokenInfoMother;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class TokenBalanceMother {
    public static TokenBalance fndResult() {
        return aViewTokenBalanceResult("10000000283000000000000", TokenInfoMother.fnd().build());
    }

    public static TokenBalance daiResult() {
        return aViewTokenBalanceResult("10000000283000000000000", TokenInfoMother.dai().build());
    }

    public static TokenBalance zrxResult() {
        return aViewTokenBalanceResult("655000000000000000000", TokenInfoMother.zrx().build());
    }

    public static TokenBalance vthoResult() {
        return aViewTokenBalanceResult("1006500000000000000000", TokenInfoMother.vtho().build());
    }

    public static TokenBalance shaResult() {
        return aViewTokenBalanceResult("606500000000000000000", TokenInfoMother.sha().build());
    }

    public static TokenBalance imxResult() {
        return TokenBalance.builder()
                .tokenAddress("0xdB36eEf120a34DC1c966506e3592c41389493953b")
                .name("ImmutableX")
                .symbol("IMX")
                .rawBalance("10000000000000000000")
                .balance(10D)
                .decimals(18)
                .transferable(true)
                .type("ERC20")
                .logo("https://res.cloudinary.com/nenvy-llc/image/upload/f_auto,q_25,c_scale,w_194/f_auto,q_25,c_scale,w_194,u_veefriends:specials:book:bubble:empathy-drink-it.jpg/v1/veefriends/specials/book/frames/gold.png")
                .build();
    }

    public static TokenBalance aViewTokenBalanceResult(final String rawBalanceAsString, final TokenInfo tokenInfo) {
        final BigInteger rawBalance = new BigInteger(rawBalanceAsString);
        return TokenBalance.builder()
                           .tokenAddress(tokenInfo.getAddress())
                           .rawBalance(rawBalance.toString())
                           .balance(calculateBalance(rawBalance, tokenInfo))
                           .decimals(tokenInfo.getDecimals())
                           .transferable(tokenInfo.isTransferable())
                           .type(tokenInfo.getType())
                           .symbol(tokenInfo.getSymbol())
                           .name(tokenInfo.getName())
                           .build();
    }

    private static double calculateBalance(final BigInteger tokenBalance, final TokenInfo tokenInfo) {
        final BigDecimal rawBalance = new BigDecimal(tokenBalance);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue();
    }
}
