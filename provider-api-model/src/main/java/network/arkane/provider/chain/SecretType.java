package network.arkane.provider.chain;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Secret types, also known as chains, should be added here.
 * Supported chains
 * - AETERNITY
 * - BITCOIN
 * - ETHEREUM
 * - GOCHAIN
 * - LITECOIN
 * - TRON
 * - VECHAIN
 * - NEO
 */
public enum SecretType {
    AETERNITY(Values.AETERNITY),
    BITCOIN(Values.BITCOIN),
    ETHEREUM(Values.ETHEREUM),
    GOCHAIN(Values.GOCHAIN),
    LITECOIN(Values.LITECOIN),
    TRON(Values.TRON),
    VECHAIN(Values.VECHAIN),
    MATIC(Values.MATIC),
    NEO(Values.NEO);

    SecretType(final String stringValue) {
        if (!this.name().equals(stringValue)) {
            throw new IllegalStateException("Incorrect String value for SecretType");
        }
    }

    public static String getAllTypes() {
        return Stream.of(SecretType.values()).map(Enum::name).collect(Collectors.joining(", "));
    }

    public static class Values {
        public static final String AETERNITY = "AETERNITY";
        public static final String BITCOIN = "BITCOIN";
        public static final String ETHEREUM = "ETHEREUM";
        public static final String GOCHAIN = "GOCHAIN";
        public static final String LITECOIN = "LITECOIN";
        public static final String TRON = "TRON";
        public static final String VECHAIN = "VECHAIN";
        public static final String NEO = "NEO";
        public static final String MATIC = "MATIC";
    }
}
