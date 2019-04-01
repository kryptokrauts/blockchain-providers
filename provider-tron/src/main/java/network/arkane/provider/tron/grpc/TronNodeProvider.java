package network.arkane.provider.tron.grpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class TronNodeProvider {

    private List<String> fullNodes;
    private List<String> solidityNodes;
    private Random random = new Random();

    public TronNodeProvider(final Environment environment) {
        if (environment.getProperty("network.arkane.tron.fullnode.url") == null) {
            throw new IllegalArgumentException("Please provide a list of tron full node urls (network.arkane.tron.fullnodes.url)");
        }
        if (environment.getProperty("network.arkane.tron.soliditynode.url") == null) {
            throw new IllegalArgumentException("Please provide a list of tron solidity node urls (network.arkane.tron.soliditynode.url)");
        }

        this.fullNodes = Stream.of(environment.getProperty("network.arkane.tron.fullnode.url").split(",")).map(String::trim).collect(Collectors.toList());
        this.solidityNodes = Stream.of(environment.getProperty("network.arkane.tron.soliditynode.url").split(",")).map(String::trim).collect(Collectors.toList());
    }

    public String randomSolidityNode() {
        return solidityNodes.get(random.nextInt(solidityNodes.size()));
    }

    public String randomFullNode() {
        return fullNodes.get(random.nextInt(fullNodes.size()));
    }
}


