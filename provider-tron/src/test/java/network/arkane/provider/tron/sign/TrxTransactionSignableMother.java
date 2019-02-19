package network.arkane.provider.tron.sign;

class TrxTransactionSignableMother {

    public static TrxTransactionSignable trxTransactionSignable() {
        return TrxTransactionSignable.builder()
                                     .data("0x")
                                     .amount(1000L)
                                     .to("TQ69Jy7jTM12MnqBQZNuaZJWY9nLszAheq")
                                     .build();
    }

}