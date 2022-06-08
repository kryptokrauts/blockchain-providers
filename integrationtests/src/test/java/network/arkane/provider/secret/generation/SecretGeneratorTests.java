package network.arkane.provider.secret.generation;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.BlockProvidersIT;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BlockProvidersIT.class)
@Slf4j
public class SecretGeneratorTests {

    @Autowired
    @Lazy
    private List<SecretGenerator<? extends SecretKey>> secretGenerators;

    @Test
    void secretGeneratorsShouldBeProvidedForEverySecretType() {
        final Map<SecretType, SecretGenerator> collect = secretGenerators.stream().collect(Collectors.toMap(SecretGenerator::type, Function.identity()));
        final long count = Stream.of(SecretType.values())
                                 .filter(type -> type != SecretType.IMX)
                                 .filter(type -> collect.get(type) == null)
                                 .peek(type -> log.error("An implementation of TransactionGateway does not exist yet for SecretType.{}", type))
                                 .count();
        if (count > 0) {
            fail(String.format("A total of %d implementation(s) of TransactionGateway were not implemented", count));
        }
    }
}
