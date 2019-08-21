package network.arkane.provider.core.model.clients;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.clients.TransactionClient;
import network.arkane.provider.core.model.blockchain.ContractCall;
import network.arkane.provider.core.model.clients.base.AbiDefinition;
import org.bouncycastle.util.encoders.Hex;

@Slf4j
public class ERC1155ContractClient extends TransactionClient {

    @SneakyThrows
    public static boolean isERC1155Contract(final String contractAddress) {
        try {
            final byte[] erc1155Bytes = Hex.decode("d9b67a26");
            final AbiDefinition abiDefinition = ERC1155Contract.defaultERC1155Contract.findAbiDefinition("supportsInterface");
            final ContractCall call = ERC1155Contract.buildCall(abiDefinition, (Object) erc1155Bytes);
            return Boolean.valueOf(callContract(call, Address.fromHexString(contractAddress), Revision.BEST).getData());
        } catch (final Exception ex) {
            log.debug("Unable to fetch supportsInterface for {}", contractAddress);
            return false;
        }
    }
}
