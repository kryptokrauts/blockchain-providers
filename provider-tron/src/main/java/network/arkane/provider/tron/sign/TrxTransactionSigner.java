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
public class TrxTransactionSigner extends TronTransactionSigner<TrxTransactionSignable, TronSecretKey> {

    private BlockGateway blockGateway;

    public TrxTransactionSigner(BlockGateway blockGateway) {
        this.blockGateway = blockGateway;
    }

    @Override
    public Signature createSignature(final TrxTransactionSignable signable,
                                     final TronSecretKey key) {
        log.info("Creating signature for: {}", signable);
        final Protocol.Transaction transaction = createTransaction(key.getKeyPair().getAddress(), GrpcClient.decodeFromBase58Check(signable.getTo()), signable.getAmount());
        final byte[] signature = signTransaction2Byte(transaction.toByteArray(), key.getKeyPair().getPrivKeyBytes());
        return TransactionSignature.signTransactionBuilder()
                                   .signedTransaction(Hex.encodeHexString(signature))
                                   .build();
    }

    public Protocol.Transaction createTransaction(byte[] from, byte[] to, long amount) {
        final Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        final Protocol.Block newestBlock = blockGateway.getBlock(-1);

        Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        Contract.TransferContract.Builder transferContractBuilder = Contract.TransferContract
                .newBuilder();
        transferContractBuilder.setAmount(amount);
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsOwner = ByteString.copyFrom(from);
        transferContractBuilder.setToAddress(bsTo);
        transferContractBuilder.setOwnerAddress(bsOwner);
        try {
            Any any = Any.pack(transferContractBuilder.build());
            contractBuilder.setParameter(any);
        } catch (Exception e) {
            log.error("An error occurred trying to create a TRON-signature");
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message("An error occurred trying to create a TRON-signature")
                                 .errorCode("tron.signature.error")
                                 .build();
        }
        contractBuilder.setType(Protocol.Transaction.Contract.ContractType.TransferContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder)
                          .setTimestamp(getCurrentTime())
                          .setExpiration(getExpiration(newestBlock));
        final Protocol.Transaction transaction = transactionBuilder.build();
        return setReference(transaction, newestBlock);
    }
}
