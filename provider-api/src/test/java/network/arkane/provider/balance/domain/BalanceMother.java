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
}