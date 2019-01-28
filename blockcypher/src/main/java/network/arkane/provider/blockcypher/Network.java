package network.arkane.provider.blockcypher;

public enum Network {
    BTC("btc", "main"),
    BTC_TEST("btc", "test3"),
    DOGECOIN("doge", "main"),
    LITECOIN("ltc", "main");

    private final String coin;
    private final String chain;

    Network(String coin, String chain) {
        this.coin = coin;
        this.chain = chain;
    }

    public String getCoin() {
        return coin;
    }

    public String getChain() {
        return chain;
    }
}
