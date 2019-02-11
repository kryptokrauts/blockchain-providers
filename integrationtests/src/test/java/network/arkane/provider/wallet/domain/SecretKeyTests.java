package network.arkane.provider.wallet.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Set;
import java.util.function.Consumer;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = BlockProvidersIT.class)
@Slf4j
public class SecretKeyTests {

    @Test
    void name() throws Exception {
        final Reflections reflections = new Reflections("network.arkane");
        final Set<Class<? extends SecretKey>> subTypes = reflections.getSubTypesOf(SecretKey.class);

        subTypes.forEach(checkImplementation());
    }

    private Consumer<Class<? extends SecretKey>> checkImplementation() {
        return x -> {
            try {
                SecretKey secretKey = x.newInstance();
            } catch (final Exception ex) {

            }
        };
    }
}
