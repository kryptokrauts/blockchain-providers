package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.BlockProvidersIT;
import network.arkane.provider.chain.SecretType;
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
public class BalanceGatewayTests {

    @Autowired
    @Lazy
    private List<BalanceGateway> balanceGateways;

    @Test
    void balanceGatewaysShouldBeProvidedForEverySecretType() {

        final Map<SecretType, BalanceGateway> collect = balanceGateways.stream()
                                                                       .collect(Collectors.toMap(BalanceGateway::type, Function.identity()));
        final long count = Stream.of(SecretType.values())
                                 .filter(type -> collect.get(type) == null)
                                 .peek(type -> log.error("An implementation of BalanceGateway does not exist yet for SecretType.{}", type))
                                 .count();
        if (count > 0) {
            fail(String.format("A total of %d implementation(s) of BalanceGateway were not implemented", count));
        }
    }
}
