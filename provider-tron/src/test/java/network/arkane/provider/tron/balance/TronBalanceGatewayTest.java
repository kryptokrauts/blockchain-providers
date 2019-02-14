package network.arkane.provider.tron.balance;

import com.google.protobuf.ByteString;
import network.arkane.provider.tron.grpc.GrpcClient;
import org.junit.jupiter.api.Test;
import org.tron.core.Constant;
import org.tron.core.Wallet;
import org.tron.protos.Protocol;

class TronBalanceGatewayTest {


    @Test
    void name() {
        Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_MAINNET);
        Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_MAINNET);
        GrpcClient grpcClient = new GrpcClient("grpc.trongrid.io:50051", "grpc.trongrid.io:50052");
        final byte[] bytes = GrpcClient.decodeFromBase58Check("TLLM21wteSPs4hKjbxgmH1L6poyMjeTbHm");
        Protocol.Account result = grpcClient.getBlockingStubSolidity()
                                            .getAccount(Protocol.Account.newBuilder().setAddress(ByteString.copyFrom(bytes)).build());
        System.out.println(result);
    }
}