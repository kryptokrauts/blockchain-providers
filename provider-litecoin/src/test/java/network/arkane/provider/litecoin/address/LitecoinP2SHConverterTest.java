package network.arkane.provider.litecoin.address;


import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LitecoinP2SHConverterTest {

    LitecoinP2SHConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LitecoinP2SHConverter(
                new LitecoinEnv(Network.LITECOIN, new LitecoinParams())
        );
    }

    @Test
    void doNotConvertAddressesPrefixedWithL() {
        String address = "LPvoR2VhDVCM7ii3yAAcnKSH13Db5HssMk";

        String result = converter.convert(address);

        assertThat(result).isEqualTo(address);
    }

    @Test
    void doNotConvertAddressesPrefixedWith3() {
        String address = "3PbLsCPghuXCSomZba4r3eEYbywQgjT9NV";

        String result = converter.convert(address);

        assertThat(result).isEqualTo(address);
    }

    @Test
    void convertAddressesPrefixedWithM() {
        String address = "MVoVB5oef2NdFK3ThT4BsHUwvgXrjd1zwJ";

        String result = converter.convert(address);

        assertThat(result).isEqualTo("3PbLsCPghuXCSomZba4r3eEYbywQgjT9NV");
    }

    @Test
    void senderAddressIsNotAcceptableVersion() {
        assertThatThrownBy(() ->
                converter.convert("1DSfKJ8rPEGW1HkvEnNCozwXB4itn2a4Bh")

        )
                .hasMessageStartingWith("Version code of address did not match acceptable versions for network")
                .hasFieldOrPropertyWithValue("errorCode", "litecoin.address-wrong-network")
                .isInstanceOf(ArkaneException.class);
    }

}