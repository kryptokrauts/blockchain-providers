package network.arkane.provider.token;

public class TokenInfoMother {

    private TokenInfoMother() {
    }

    public static TokenInfo.TokenInfoBuilder fnd() {
        return TokenInfo.builder()
                        .name("FundRequest")
                        .symbol("FND")
                        .type("ERC20")
                        .transferable(true)
                        .address("0x4df47b4969b2911c966506e3592c41389493953b")
                        .decimals(18);
    }

    public static TokenInfo.TokenInfoBuilder dai() {
        return TokenInfo.builder()
                        .name("Dai Stablecoin v1.0")
                        .symbol("DAI")
                        .type("ERC20")
                        .transferable(true)
                        .address("0x89d24A6b4CcB1B6fAA2625fE562bDD9a23260359")
                        .decimals(18);
    }

    public static TokenInfo.TokenInfoBuilder zrx() {
        return TokenInfo.builder()
                        .name("0x Project")
                        .symbol("ZRX")
                        .transferable(true)
                        .type("ERC20")
                        .address("0xE41d2489571d322189246DaFA5ebDe1F4699F498")
                        .decimals(18);
    }

    public static TokenInfo.TokenInfoBuilder vtho() {
        return TokenInfo.builder()
                        .name("TokenVeThor")
                        .symbol("VTHO")
                        .transferable(true)
                        .address("0x0000000000000000000000000000456E65726779")
                        .type("VIP180")
                        .decimals(18);
    }

    public static TokenInfo.TokenInfoBuilder sha() {
        return TokenInfo.builder()
                        .name("SafeHaven")
                        .symbol("SHA")
                        .transferable(true)
                        .type("VIP180")
                        .address("0x5db3C8A942333f6468176a870dB36eEf120a34DC")
                        .decimals(18);
    }

    public static TokenInfo.TokenInfoBuilder mkr() {
        return TokenInfo.builder()
                        .name("Maker")
                        .symbol("MKR")
                        .transferable(true)
                        .address("0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2")
                        .decimals(18)
                        .type("ERC20");
    }
}