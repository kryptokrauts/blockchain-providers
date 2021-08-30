package network.arkane.provider.tron.block;

import network.arkane.provider.tron.grpc.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tron.protos.Protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlockGatewayTest {

    private BlockGateway blockGateway;
    private GrpcClient grpcClient;

    @BeforeEach
    void setUp() {
        this.grpcClient = mock(GrpcClient.class);
        blockGateway = new BlockGateway(grpcClient);
    }

    @Test
    void getBlock() {
        final Protocol.Block block = Protocol.Block.getDefaultInstance();
        final int blockNumber = -1;
        when(grpcClient.getBlock(blockNumber)).thenReturn(block);
        assertThat(blockGateway.getBlock(blockNumber)).isEqualTo(block);
    }
}