package network.arkane.provider.neo;

import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoBlockCount;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class NeoW3JConfigurationTest {

    @Test
    void getsBlockCount() throws IOException {
        Neow3j neow3j = new NeoW3JConfiguration().neoNeow3j("https://neo.arkane.network");
        NeoBlockCount blockCountReq = neow3j.getBlockCount().send();

        long actual = blockCountReq.getBlockIndex().longValue();
        System.out.println(actual);
        assertThat(actual).isGreaterThan(0);
    }
}
