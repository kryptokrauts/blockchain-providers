package network.arkane.provider.core.model.clients;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.clients.TransactionClient;
import network.arkane.provider.core.model.blockchain.ContractCall;
import network.arkane.provider.core.model.clients.base.AbiDefinition;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @SneakyThrows
    public static List<BigInteger> balanceOf(final String contractAddress,
                                             final String owner,
                                             final List<BigInteger> ids) {
        final AbiDefinition abiDefinition = ERC1155Contract.defaultERC1155Contract.findAbiDefinition("balanceOfBatch");
        final String[] owners = IntStream.range(0, ids.size())
                                         .mapToObj(x -> owner)
                                         .collect(Collectors.toList())
                                         .toArray(new String[] {});
        final ContractCall call = ERC1155Contract.buildCall(abiDefinition,
                                                            owners,
                                                            ids.toArray());
        final String data = callContract(call, Address.fromHexString(contractAddress), Revision.BEST).getData();
        Function f = new Function("balanceOfBatch",
                                  Arrays.asList(new DynamicArray<org.web3j.abi.datatypes.Address>(
                                          Arrays.stream(owners)
                                                .map(org.web3j.abi.datatypes.Address::new).collect(Collectors.toList())
                                  ), new DynamicArray<>(
                                          ids.stream().map(Uint256::new).collect(Collectors.toList()))),
                                  Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return ((ArrayList<Uint256>) FunctionReturnDecoder.decode(data, f.getOutputParameters())
                                                          .get(0).getValue())
                .stream()
                .map(NumericType::getValue)
                .collect(Collectors.toList());
    }
}
