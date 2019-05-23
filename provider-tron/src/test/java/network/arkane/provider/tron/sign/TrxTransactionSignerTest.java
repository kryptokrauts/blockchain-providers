package network.arkane.provider.tron.sign;

import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.tron.block.BlockGateway;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tron.core.Constant;
import org.tron.core.Wallet;
import org.tron.protos.Protocol;

import static network.arkane.provider.tron.secret.generation.TronSecretKeyMother.aRandomSecretKey;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrxTransactionSignerTest {

    private TrxTransactionSigner trxTransactionSigner;
    private BlockGateway blockGateway;

    @BeforeEach
    void setUp() {
        Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_MAINNET);
        Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_MAINNET);
        this.blockGateway = mock(BlockGateway.class);
        trxTransactionSigner = new TrxTransactionSigner(blockGateway);
    }

    @Test
    void createSignature() throws Exception {
        when(blockGateway.getBlock(-1)).thenReturn(getBlock());
        Signature signature = trxTransactionSigner.createSignature(TrxTransactionSignableMother.trxTransactionSignable(), aRandomSecretKey());
        assertThat(signature).isInstanceOf(TransactionSignature.class);
        Protocol.Transaction parsedTx = Protocol.Transaction.parseFrom(Hex.decodeHex(((TransactionSignature) signature).getSignedTransaction()));
        assertThat(parsedTx.getRawData().getExpiration()).isEqualTo(36000001);
        assertThat(parsedTx.getRawData().getContract(0).getType()).isEqualTo(Protocol.Transaction.Contract.ContractType.TransferContract);
    }

    private Protocol.Block getBlock() {
        return Protocol.Block.newBuilder()
                             .setBlockHeader(Protocol.BlockHeader.newBuilder()
                                                                 .setRawData(Protocol.BlockHeader.raw.newBuilder()
                                                                                                     .setNumber(1)
                                                                                                     .setTimestamp(1)
                                                                                                     .build())
                                                                 .build())
                             .build();
    }
}