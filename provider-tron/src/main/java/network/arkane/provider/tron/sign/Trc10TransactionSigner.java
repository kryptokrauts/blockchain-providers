package network.arkane.provider.tron.sign;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.tron.block.BlockGateway;
import network.arkane.provider.tron.grpc.GrpcClient;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

@Component
@Slf4j
public class Trc10TransactionSigner extends TronTransactionSigner<Trc10TransactionSignable, TronSecretKey> {

    private BlockGateway blockGateway;

    public Trc10TransactionSigner(BlockGateway blockGateway) {
        this.blockGateway = blockGateway;
    }

    @Override
    public Signature createSignature(Trc10TransactionSignable signable, TronSecretKey key) {
        final Protocol.Transaction transaction = createTransaction(signable.getToken(),
                                                                   key.getKeyPair().getAddress(),
                                                                   GrpcClient.decodeFromBase58Check(signable.getTo()),
                                                                   signable.getAmount());
        final byte[] signature = signTransaction2Byte(transaction.toByteArray(), key.getKeyPair().getPrivKeyBytes());
        return TransactionSignature.signTransactionBuilder()
                                   .signedTransaction(Hex.encodeHexString(signature))
                                   .build();
    }

    public Protocol.Transaction createTransaction(final String token, final byte[] from, byte[] to, long amount) {
        final Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        final Protocol.Block newestBlock = blockGateway.getBlock(-1);

        Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        Contract.TransferAssetContract.Builder transferContractBuilder = Contract.TransferAssetContract
                .newBuilder();
        transferContractBuilder.setAmount(amount);
        final ByteString bsTo = ByteString.copyFrom(to);
        final ByteString bsOwner = ByteString.copyFrom(from);
        transferContractBuilder.setToAddress(bsTo);
        transferContractBuilder.setOwnerAddress(bsOwner);
        transferContractBuilder.setAssetName(ByteString.copyFromUtf8(token));
        try {
            Any any = Any.pack(transferContractBuilder.build());
            contractBuilder.setParameter(any);
        } catch (Exception e) {
            log.error("An error occurred trying to create a TRON-asset-signature");
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message("An error occurred trying to create a TRON-asset-signature")
                                 .errorCode("tron.signature.error")
                                 .build();
        }
        contractBuilder.setType(Protocol.Transaction.Contract.ContractType.TransferAssetContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder)
                          .setTimestamp(System.currentTimeMillis())
                          .setExpiration(newestBlock.getBlockHeader().getRawData().getTimestamp() + 10 * 60 * 60 * 1000);
        final Protocol.Transaction transaction = transactionBuilder.build();
        return setReference(transaction, newestBlock);
    }
}
