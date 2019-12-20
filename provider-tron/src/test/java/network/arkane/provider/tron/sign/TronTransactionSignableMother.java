package network.arkane.provider.tron.sign;

import java.math.BigInteger;

class TronTransactionSignableMother {

    public static TronTransactionSignable tronTransactionSignable() {
        return TronTransactionSignable.builder()
                                      .data("0x")
                                      .amount(BigInteger.valueOf(1000))
                                      .to("TQ69Jy7jTM12MnqBQZNuaZJWY9nLszAheq")
                                      .build();
    }

}
