package network.arkane.provider.tron.tx;

import network.arkane.provider.tron.grpc.GrpcClient;
import network.arkane.provider.tron.grpc.TronNodeProvider;
import network.arkane.provider.tx.TxInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.tron.core.Constant;
import org.tron.core.Wallet;

import static org.assertj.core.api.Assertions.assertThat;

class TronTransactionInfoServiceIntegrationTest {

    private TronTransactionInfoService infoService;

    @BeforeEach
    void setUp() {
        Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_TESTNET);
        Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_TESTNET);
        MockEnvironment env = new MockEnvironment();
        env.setProperty("network.arkane.tron.fullnode.url", "grpc.shasta.trongrid.io:50051");
        env.setProperty("network.arkane.tron.soliditynode.url", "grpc.shasta.trongrid.io:50052");
        env.setProperty("tron.network", "testnet");

        TronNodeProvider tronNodeProvider = new TronNodeProvider(env);

        GrpcClient grpcClient = new GrpcClient(tronNodeProvider);
        grpcClient.updateNodeAvailability();
        infoService = new TronTransactionInfoService(grpcClient);
    }

    @Test
    void getTransferInfo() {
        TxInfo txInfo = infoService.getTransaction("c31f3c660fc50425e80c12ff9192ead656b0a82543880888eeb4161d64b6b933");

        assertThat(txInfo).isNotNull();
    }

    @Test
    void getTriggerSmartContractInfo() {
        TxInfo txInfo = infoService.getTransaction("b3e9c5fff6c27df10a9a1c3a48ea87091f4be3c507ec73393a6432419d222696");

        assertThat(txInfo).isNotNull();
    }
}