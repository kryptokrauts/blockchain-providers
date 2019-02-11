package network.arkane.provider.tron;

import io.grpc.Grpc;
import network.arkane.provider.tron.grpc.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tron.core.Constant;
import org.tron.core.Wallet;

@Configuration
public class TronAutoConfiguration {

    private final String fullNode;
    private final String solidityNode;

    public TronAutoConfiguration(final @Value("${network.arkane.tron.fullnode.url:'grpc.trongrid.io:50051'}") String fullNode, final @Value("${network.arkane.tron.soliditynode.url:'grpc.trongrid.io:50052'}") String solidityNode, final String environment) {
        this.fullNode = fullNode;
        this.solidityNode = solidityNode;
        if ("testnet".equals(environment)) {
            Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_TESTNET);
            Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_TESTNET);
        } else {
            Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_MAINNET);
            Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_MAINNET);
        }
    }

    @Bean
    public GrpcClient client() {
        return new GrpcClient(fullNode, solidityNode);
    }
}
