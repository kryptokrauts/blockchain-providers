package network.arkane.provider.tron.block;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.tron.grpc.GrpcClient;
import org.springframework.stereotype.Component;
import org.tron.protos.Protocol.Block;

@Component
@Slf4j
public class BlockGateway {

    private GrpcClient grpcClient;

    public BlockGateway(final GrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    public Block getBlock(final int blockNumber) {
        log.debug("Getting tronblock {}", blockNumber);
        return grpcClient.getBlock(blockNumber);
    }
}
