package network.arkane.provider.clients;


import network.arkane.provider.clients.base.AbstractClient;
import network.arkane.provider.core.model.blockchain.Block;
import network.arkane.provider.core.model.clients.Revision;

import java.io.IOException;
import java.util.HashMap;

public class BlockClient extends AbstractClient {

    /**
     * Get {@link Block} information.
     *
     * @param revision {@link Revision} optional the block revision, can be null.
     * @return Block {@link Block} can be null.
     * @throws ClientIOException
     */
    public static Block getBlock(Revision revision) throws IOException {
        Revision currentRevision = revision;
        if (revision == null) {
            currentRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[] {"revision"}, new String[] {currentRevision.toString()});
        return sendGetRequest(Path.GetBlockPath, uriParams, null, Block.class);
    }
}
