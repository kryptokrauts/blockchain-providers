package network.arkane.provider.tron.block;

import network.arkane.provider.tron.grpc.GrpcClient;
import org.springframework.stereotype.Component;
import org.tron.protos.Protocol.Block;

@Component
public class BlockGateway {

    private GrpcClient grpcClient;

    public BlockGateway(final GrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    public Block getBlock(final int blockNumber) {
        return grpcClient.getBlock(blockNumber);
    }
}
