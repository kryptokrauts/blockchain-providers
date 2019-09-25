package network.arkane.provider.contract;

import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AbiTypeFactoryTest {

    @Test
    void arrays() {
        Type result = AbiTypesFactory.getType("address[]", "[0xa, 0xb, 0xc]");

        assertThat(result.getTypeAsString()).isEqualTo("address[]");
        assertThat(result.getValue()).isInstanceOf(List.class);
        assertThat(((List) result.getValue())).containsExactlyInAnyOrder(new Address("0x000000000000000000000000000000000000000a"),
                                                                         new Address("0x000000000000000000000000000000000000000b"),
                                                                         new Address("0x000000000000000000000000000000000000000c"));
    }
}