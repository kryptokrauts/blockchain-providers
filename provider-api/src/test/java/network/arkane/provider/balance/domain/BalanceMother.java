package network.arkane.provider.balance.domain;

import network.arkane.provider.chain.SecretType;

public class BalanceMother {
    public static Balance etherBalance() {
        return Balance.builder()
                      .balance(1)
                      .rawBalance("1000000000000000000")
                      .gasBalance(1)
                      .rawGasBalance("1000000000000000000")
                      .secretType(SecretType.ETHEREUM)
                      .decimals(18)
                      .build();
    }

    public static Balance imxBalance() {
        return Balance.builder()
                      .gasSymbol("IMX")
                      .symbol("IMX")
                      .balance(12.34)
                      .gasBalance(0.12)
                      .secretType(SecretType.IMX)
                      .rawBalance("1")
                      .rawGasBalance("1")
                      .decimals(14)
                      .build();
    }
}