package network.arkane.provider.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.BlockProvidersIT;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static network.arkane.provider.chain.SecretType.AETERNITY;
import static network.arkane.provider.chain.SecretType.BITCOIN;
import static network.arkane.provider.chain.SecretType.GOCHAIN;
import static network.arkane.provider.chain.SecretType.LITECOIN;
import static network.arkane.provider.chain.SecretType.NEO;
import static network.arkane.provider.chain.SecretType.TRON;
import static network.arkane.provider.chain.SecretType.VECHAIN;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BlockProvidersIT.class})
public class NonFungibleGatewayTests {

    private static final Set<SecretType> IGNORED_TYPES = Set.of(AETERNITY,
                                                                BITCOIN,
                                                                GOCHAIN,
                                                                LITECOIN,
                                                                TRON,
                                                                VECHAIN,
                                                                NEO);

    @Autowired
    @Lazy
    private List<NonFungibleGateway> nonFungibleGateways;

    @Test
    void implementationShouldBeProvidedForEverySecretType() {

        final Map<SecretType, NonFungibleGateway> collect = nonFungibleGateways.stream()
                                                                               .collect(Collectors.toMap(NonFungibleGateway::getSecretType, Function.identity()));
        final long count = Stream.of(SecretType.values())
                                 .filter(type -> !IGNORED_TYPES.contains(type))
                                 .filter(type -> collect.get(type) == null)
                                 .peek(type -> log.error("An implementation of NonFungibleGateway does not exist yet for SecretType.{}", type))
                                 .count();
        if (count > 0) {
            fail(String.format("A total of %d required implementation(s) of NonFungibleGateway were not implemented", count));
        }
    }
}
