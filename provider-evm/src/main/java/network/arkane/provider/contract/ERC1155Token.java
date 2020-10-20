package network.arkane.provider.contract;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public final class ERC1155Token extends Contract {

    public ERC1155Token(final String contractAddress,
                        final Web3j web3j) {
        super("", contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ZERO)), BigInteger.ZERO, BigInteger.valueOf(8000000));
    }

    public List<BigInteger> balanceOfBatch(final String owner,
                                           final List<BigInteger> ids) {
        try {
            final String[] owners = (String[]) ((List) IntStream.range(0, ids.size()).mapToObj((x) -> owner).collect(Collectors.toList())).toArray(new String[0]);
            final Function f = new Function("balanceOfBatch",
                                            Arrays.asList(new DynamicArray((List) Arrays.stream(owners).map(org.web3j.abi.datatypes.Address::new).collect(Collectors.toList())),
                                                          new DynamicArray((List) ids.stream().map(Uint256::new).collect(Collectors.toList()))),
                                            Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {
                                            }));

            final List<Uint256> listRemoteFunctionCall = executeRemoteCallSingleValueReturn(f, List.class).send();
            return listRemoteFunctionCall
                    .stream()
                    .map(NumericType::getValue)
                    .collect(Collectors.toList());
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }
}
