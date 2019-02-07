package network.arkane.provider.tron;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.tron.core.Constant;
import org.tron.core.Wallet;

@Configuration
public class TronAutoConfiguration {

    public TronAutoConfiguration(final @Value("${network.arkane.tron.fullnode.url:'grpc.trongrid.io:50051'}") String fullNode, final @Value("${network.arkane.tron.soliditynode.url:'grpc.trongrid.io:50052'}") String solidityNode, final String environment) {
        if ("testnet".equals(environment)) {
            Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_TESTNET);
            Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_TESTNET);
        } else {
            Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_MAINNET);
            Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_MAINNET);
        }
    }
}
