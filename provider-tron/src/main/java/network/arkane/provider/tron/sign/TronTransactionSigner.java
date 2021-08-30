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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.tron.protos.Protocol;
import org.tron.protos.contract.BalanceContract;
import org.tron.protos.contract.SmartContractOuterClass;

@Component
@Slf4j
public class TronTransactionSigner extends AbstractTronTransactionSigner<TronTransactionSignable, TronSecretKey> {

    private BlockGateway blockGateway;

    public TronTransactionSigner(BlockGateway blockGateway) {
        this.blockGateway = blockGateway;
    }

    @Override
    public Signature createSignature(final TronTransactionSignable signable,
                                     final TronSecretKey key) {
        log.info("Creating signature for: {}", signable);
        Protocol.Transaction transaction;
        if (StringUtils.isNotBlank(signable.getData()) && signable.getData().replaceFirst("0x", "").length() > 0) {
            transaction = createContractExecution(key.getKeyPair().getAddress(),
                                                  GrpcClient.decodeFromBase58Check(signable.getTo()),
                                                  signable.getAmount().longValue(),
                                                  signable.getData());
        } else {
            transaction = createTransaction(key.getKeyPair().getAddress(), GrpcClient.decodeFromBase58Check(signable.getTo()), signable.getAmount().longValue());
        }

        final byte[] signature = signTransaction2Byte(transaction.toByteArray(), key.getKeyPair().getPrivKeyBytes());
        log.info("Done creating signature: {}", Hex.encodeHexString(signature));
        return TransactionSignature.signTransactionBuilder()
                                   .signedTransaction(Hex.encodeHexString(signature))
                                   .build();
    }

    public Protocol.Transaction createTransaction(byte[] from,
                                                  byte[] to,
                                                  long amount) {
        final Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        final Protocol.Block newestBlock = blockGateway.getBlock(-1);

        Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        BalanceContract.TransferContract.Builder transferContractBuilder = BalanceContract.TransferContract
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
                          .setFeeLimit(100000000)
                          .setExpiration(getExpiration(newestBlock));
        final Protocol.Transaction transaction = transactionBuilder.build();
        return setReference(transaction, newestBlock);
    }

    public Protocol.Transaction createContractExecution(byte[] from,
                                                        byte[] to,
                                                        long amount,
                                                        String data) {
        final Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        final Protocol.Block newestBlock = blockGateway.getBlock(-1);
        if (data.startsWith("0x")) {
            data = data.substring(2);
        }

        Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        SmartContractOuterClass.TriggerSmartContract.Builder triggerContractBuilder = SmartContractOuterClass.TriggerSmartContract.newBuilder();
        triggerContractBuilder.setCallValue(amount);
        triggerContractBuilder.setData(ByteString.copyFrom(org.bouncycastle.util.encoders.Hex.decode(data)));
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsOwner = ByteString.copyFrom(from);
        triggerContractBuilder.setContractAddress(bsTo);
        triggerContractBuilder.setOwnerAddress(bsOwner);
        try {
            Any any = Any.pack(triggerContractBuilder.build());
            contractBuilder.setParameter(any);
        } catch (Exception e) {
            log.error("An error occurred trying to create a TRON-signature (Contract execution)");
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message("An error occurred trying to create a TRON-signature (Contract execution)")
                                 .errorCode("tron.signature.error")
                                 .build();
        }
        contractBuilder.setType(Protocol.Transaction.Contract.ContractType.TriggerSmartContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder)
                          .setTimestamp(getCurrentTime())
                          .setFeeLimit(100000000)
                          .setExpiration(getExpiration(newestBlock));
        final Protocol.Transaction transaction = transactionBuilder.build();
        return setReference(transaction, newestBlock);
    }
}
