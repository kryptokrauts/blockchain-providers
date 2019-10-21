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
    AETERNITY, BITCOIN, ETHEREUM, GOCHAIN, LITECOIN, TRON, VECHAIN, NEO;

    public static String getAllTypes() {
        return Stream.of(SecretType.values()).map(Enum::name).collect(Collectors.joining(", "));
    }
}
