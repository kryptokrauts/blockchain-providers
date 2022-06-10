package network.arkane.provider.chain;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Secret types, also known as chains, should be added here.
 * Supported chains
 * - AETERNITY
 * - BITCOIN
 * - BSC (Binance Smart Chain)
 * - ETHEREUM
 * - GOCHAIN
 * - LITECOIN
 * - TRON
 * - VECHAIN
 * - NEO
 * - HEDERA
 * - IMX
 */
public enum SecretType {
    AETERNITY(Values.AETERNITY, "AE", "AE"),
    AVAC(Values.AVAC, "AVAX", "AVAX"),
    BITCOIN(Values.BITCOIN, "BTC", "BTC"),
    BSC(Values.BSC, "BNB", "BNB"),
    ETHEREUM(Values.ETHEREUM, "ETH", "ETH"),
    GOCHAIN(Values.GOCHAIN, "GO", "GO"),
    HEDERA(Values.HEDERA, "HBAR", "HBAR"),
    LITECOIN(Values.LITECOIN, "LTC", "LTC"),
    TRON(Values.TRON, "TRX", "TRX"),
    VECHAIN(Values.VECHAIN, "VET", "VTHO"),
    MATIC(Values.MATIC, "MATIC", "MATIC"),
    NEO(Values.NEO, "NEO", "GAS"),
    IMX(Values.IMX, "IMX", "IMX");

    private String symbol;
    private String gasSymbol;

    SecretType(final String stringValue,
               String symbol,
               String gasSymbol) {
        this.symbol = symbol;
        this.gasSymbol = gasSymbol;
        if (!this.name().equals(stringValue)) {
            throw new IllegalStateException("Incorrect String value for SecretType");
        }
    }

    public static String getAllTypes() {
        return Stream.of(SecretType.values()).map(Enum::name).collect(Collectors.joining(", "));
    }

    public static class Values {
        public static final String AETERNITY = "AETERNITY";
        public static final String AVAC = "AVAC";
        public static final String BITCOIN = "BITCOIN";
        public static final String BSC = "BSC";
        public static final String ETHEREUM = "ETHEREUM";
        public static final String GOCHAIN = "GOCHAIN";
        public static final String HEDERA = "HEDERA";
        public static final String LITECOIN = "LITECOIN";
        public static final String TRON = "TRON";
        public static final String VECHAIN = "VECHAIN";
        public static final String NEO = "NEO";
        public static final String MATIC = "MATIC";
        public static final String IMX = "IMX";
    }

    public String getSymbol() {
        return symbol;
    }

    public String getGasSymbol() {
        return gasSymbol;
    }
}
