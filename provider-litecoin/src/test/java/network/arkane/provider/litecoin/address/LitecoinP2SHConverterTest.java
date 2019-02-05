package network.arkane.provider.litecoin.address;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LitecoinP2SHConverterTest {

    LitecoinP2SHConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LitecoinP2SHConverter();
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
}