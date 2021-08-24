package network.arkane.blockchainproviders.covalent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import network.arkane.provider.chain.SecretType;

@AllArgsConstructor
@Getter
public enum CovalentChain {
    ETHEREUM(SecretType.ETHEREUM, 1L),
    MATIC(SecretType.MATIC, 137L),
    MATIC_TEST(SecretType.MATIC, 80001L),
    AVALANCHE(SecretType.AVAC, 43114L),
    BSC(SecretType.BSC, 56L),
    BSC_TEST(SecretType.BSC, 56L);

    private final SecretType secretType;
    private final Long chainId;

    public static CovalentChain withId(long covalentId) {
        for (CovalentChain chain : values()) {
            if (covalentId == chain.getChainId()) {
                return chain;
            }
        }
        throw new IllegalArgumentException("CovalentChain with covalentId " + covalentId + " not found");
    }


}
