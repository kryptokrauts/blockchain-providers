package network.arkane.provider.tron.sign;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.tron.block.BlockGateway;
import network.arkane.provider.tron.grpc.GrpcClient;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Sha256Hash;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

@Component
@Slf4j
public class TronTransactionSigner implements Signer<TronTransactionSignable, TronSecretKey> {

    private BlockGateway blockGateway;

    public TronTransactionSigner(BlockGateway blockGateway) {
        this.blockGateway = blockGateway;
    }

    @Override
    public Signature createSignature(final TronTransactionSignable signable,
                                     final TronSecretKey key) {
        log.info("Creating signature for: {}", signable);
        Protocol.Transaction transaction = createTransaction(key.getKeyPair().getAddress(), GrpcClient.decodeFromBase58Check(signable.getTo()), signable.getAmount());
        byte[] signature = signTransaction2Byte(transaction.toByteArray(), key.getKeyPair().getPrivKeyBytes());
        return TransactionSignature.signTransactionBuilder()
                                   .signedTransaction(Hex.encodeHexString(signature))
                                   .build();
    }

    @SneakyThrows
    private static byte[] signTransaction2Byte(byte[] transaction, byte[] privateKey) {
        final ECKey ecKey = ECKey.fromPrivate(privateKey);
        final Protocol.Transaction transaction1 = Protocol.Transaction.parseFrom(transaction);
        final byte[] rawdata = transaction1.getRawData().toByteArray();
        final byte[] hash = Sha256Hash.hash(rawdata);
        final byte[] sign = ecKey.sign(hash).toByteArray();
        return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign)).build().toByteArray();
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
                          .setTimestamp(System.currentTimeMillis())
                          .setExpiration(newestBlock.getBlockHeader().getRawData().getTimestamp() + 10 * 60 * 60 * 1000);
        final Protocol.Transaction transaction = transactionBuilder.build();
        final Protocol.Transaction refTransaction = setReference(transaction, newestBlock);
        return refTransaction;
    }

    public static Protocol.Transaction setReference(Protocol.Transaction transaction, Protocol.Block newestBlock) {
        long blockHeight = newestBlock.getBlockHeader().getRawData().getNumber();
        byte[] blockHash = getBlockHash(newestBlock).getBytes();
        byte[] refBlockNum = ByteArray.fromLong(blockHeight);
        Protocol.Transaction.raw rawData = transaction.getRawData().toBuilder()
                                                      .setRefBlockHash(ByteString.copyFrom(ByteArray.subArray(blockHash, 8, 16)))
                                                      .setRefBlockBytes(ByteString.copyFrom(ByteArray.subArray(refBlockNum, 6, 8)))
                                                      .build();
        return transaction.toBuilder().setRawData(rawData).build();
    }

    public static Sha256Hash getBlockHash(Protocol.Block block) {
        return Sha256Hash.of(block.getBlockHeader().getRawData().toByteArray());
    }
}
