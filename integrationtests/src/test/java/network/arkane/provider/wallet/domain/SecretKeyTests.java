package network.arkane.provider.wallet.domain;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class SecretKeyTests {

    @Test
    void name() throws Exception {
        final Reflections reflections = new Reflections("network.arkane");
        final Set<Class<? extends SecretKey>> subTypes = reflections.getSubTypesOf(SecretKey.class);

        final Set<SecretType> collect = subTypes.stream()
                                          .map(getClassFunction())
                                          .map(SecretKey::type)
                                          .collect(Collectors.toSet());

        long unImplementedCount = Stream.of(SecretType.values())
                           .filter(x -> !collect.contains(x))
                           .count();
        if (unImplementedCount > 0) {
            fail(String.format("A total of %d SecretTypes do not have an implemented SecretKey", unImplementedCount));
        }
    }

    private Function<Class<? extends SecretKey>, ? extends SecretKey> getClassFunction() {
        return x -> {
            try {
                return x.newInstance();
            } catch (final Exception ex) {
                throw new IllegalArgumentException("Unable to construct SecretKey");
            }
        };
    }
}
