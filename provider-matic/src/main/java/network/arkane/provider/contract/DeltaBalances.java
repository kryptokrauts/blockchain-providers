package network.arkane.provider.contract;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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

@Slf4j
public class DeltaBalances extends Contract {
    public DeltaBalances(String contractAddress,
                         Web3j web3j) {
        super(contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ONE)), BigInteger.ZERO, BigInteger.ZERO);
    }

    public List<BigInteger> tokenBalances(String user, final List<String> tokens) {
        try {
            Function f = new Function("tokenBalances",
                                      Arrays.asList(new Address(user), new DynamicArray<>(tokens.stream().map(x -> new Address(x)).collect(Collectors.toList()))),
                                      Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {}));
            final List<Uint256> send = executeRemoteCallSingleValueReturn(f, List.class).send();
            return send.stream()
                       .map(NumericType::getValue)
                       .collect(Collectors.toList());
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }
}
