package network.arkane.provider.clients;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.clients.base.AbstractClient;
import network.arkane.provider.core.model.blockchain.Block;
import network.arkane.provider.core.model.blockchain.PeerStat;
import network.arkane.provider.core.model.clients.BlockRef;
import network.arkane.provider.core.model.clients.Revision;
import network.arkane.provider.core.model.exception.ClientIOException;
import network.arkane.provider.utils.BlockchainUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Get information of blockchain. It can get block tag, block reference, nodes status of blockchain.
 */
public class BlockchainClient extends AbstractClient {

    /**
     * Get block chain tag. It is the last byte of genesis block id.
     *
     * @return byte value .
     * @throws ClientIOException if network error.
     */
    public static byte getChainTag() throws IOException {
        Block genesisBlock = BlockClient.getBlock(Revision.create(0));
        if (genesisBlock == null) {
            throw new RuntimeException(" Get Genesis block error");
        }
        String hexId = genesisBlock.getId();
        if (!BlockchainUtils.isId(hexId)) {
            throw new RuntimeException(" Genesis block id is invalid");
        }
        byte[] bytesId = BytesUtils.toByteArray(hexId);
        if (bytesId == null || bytesId.length != 32) {
            throw new RuntimeException(" Genesis block id converted error");
        }
        return bytesId[31];
    }

    /**
     * Get status of your accessing nodes on the blockchain.
     *
     * @return array of {@link PeerStat}
     * @throws ClientIOException network error.
     */
    public static ArrayList getPeerStatusList() throws IOException {

        return sendGetRequest(Path.GetNodeInfoPath, null, null, (new ArrayList<PeerStat>()).getClass());
    }

    /**
     * Get block reference from block chain node.
     *
     * @param revision optional, if set null, it will be the best block, or set {@linkplain Revision#BEST Best},
     *                 or specify block number {@linkplain Revision#create(long) create(blocknumber)}.
     * @return {@linkplain BlockRef block reference}
     * throw ClientIOException network error.
     */
    public static BlockRef getBlockRef(Revision revision) throws IOException {
        Block block = BlockClient.getBlock(revision);
        if (block != null) {
            return block.blockRef();
        } else {
            return null;
        }
    }
}
