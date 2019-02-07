package network.arkane.provider.tron.grpc;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tron.core.Constant;
import org.tron.core.Wallet;
import org.tron.protos.Protocol;

class GrpcClientTest {

    private GrpcClient grpcClient;

    @BeforeEach
    void setUp() {
        grpcClient = new GrpcClient("grpc.trongrid.io:50051", "grpc.trongrid.io:50052");
        Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_MAINNET);
        Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_MAINNET);
    }

    @Test
    void test() {
        byte[] bytes = GrpcClient.decodeFromBase58Check("TNDFkUNA2TukukC1Moeqj61pAS53NFchGF");
        grpcClient.getBlockingStubSolidity()
                  .getAccount(Protocol.Account.newBuilder().setAddress(ByteString.copyFrom(bytes)).build());

    }
}