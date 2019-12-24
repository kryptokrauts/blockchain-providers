package network.arkane.provider.tron.sign;

import com.google.protobuf.ByteString;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signable;
import network.arkane.provider.wallet.domain.SecretKey;
import org.tron.common.crypto.ECKey;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Sha256Hash;
import org.tron.protos.Protocol;

@Slf4j
abstract class AbstractTronTransactionSigner<T extends Signable, KEY extends SecretKey> implements Signer<T, KEY> {


    @SneakyThrows
    static byte[] signTransaction2Byte(byte[] transactionBytes, byte[] privateKey) {
        final ECKey ecKey = ECKey.fromPrivate(privateKey);
        final Protocol.Transaction transaction = Protocol.Transaction.parseFrom(transactionBytes);
        final byte[] rawdata = transaction.getRawData().toByteArray();
        final byte[] hash = Sha256Hash.hash(rawdata);
        final byte[] sign = ecKey.sign(hash).toByteArray();
        log.debug("Done signing for tron");
        return transaction.toBuilder().addSignature(ByteString.copyFrom(sign)).build().toByteArray();
    }

    static Protocol.Transaction setReference(Protocol.Transaction transaction, Protocol.Block newestBlock) {
        long blockHeight = newestBlock.getBlockHeader().getRawData().getNumber();
        byte[] blockHash = getBlockHash(newestBlock).getBytes();
        byte[] refBlockNum = ByteArray.fromLong(blockHeight);
        Protocol.Transaction.raw rawData = transaction.getRawData().toBuilder()
                                                      .setRefBlockHash(ByteString.copyFrom(ByteArray.subArray(blockHash, 8, 16)))
                                                      .setRefBlockBytes(ByteString.copyFrom(ByteArray.subArray(refBlockNum, 6, 8)))
                                                      .build();
        return transaction.toBuilder().setRawData(rawData).build();
    }

    private static Sha256Hash getBlockHash(Protocol.Block block) {
        return Sha256Hash.of(block.getBlockHeader().getRawData().toByteArray());
    }

    long getCurrentTime() {
        return System.currentTimeMillis();
    }

    long getExpiration(Protocol.Block newestBlock) {
        return newestBlock.getBlockHeader().getRawData().getTimestamp() + 10 * 60 * 60 * 1000;
    }
}
