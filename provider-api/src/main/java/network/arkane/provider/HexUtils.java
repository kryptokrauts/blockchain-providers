package network.arkane.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public final class HexUtils {

    private static Pattern HEXADECIMAL_PATTERN = compile("0x\\p{XDigit}+");

    private HexUtils() {
    }

    public static boolean isHex(String value) {
        final Matcher matcher = HEXADECIMAL_PATTERN.matcher(value);
        return matcher.matches();
    }

}
