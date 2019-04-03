package network.arkane.provider.tron.sign;

class Trc10TransactionSignableMother {
    public static Trc10TransactionSignable aTrc10TransactionSignable() {
        return Trc10TransactionSignable.builder()
                                       .to("TQ69Jy7jTM12MnqBQZNuaZJWY9nLszAheq")
                                       .token("1002000")
                                       .amount(1000L)
                                       .build();
    }
}