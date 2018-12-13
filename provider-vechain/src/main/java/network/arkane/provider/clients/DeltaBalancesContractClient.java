package network.arkane.provider.clients;

import network.arkane.provider.Prefix;
import network.arkane.provider.core.model.blockchain.ContractCall;
import network.arkane.provider.core.model.blockchain.ContractCallResult;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Revision;
import network.arkane.provider.core.model.clients.base.AbiDefinition;
import network.arkane.provider.core.model.clients.base.AbstractContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeltaBalancesContractClient extends TransactionClient {

    private final AbiDefinition abiDefinition;
    private final String deltaBalancesContractAddress;

    public DeltaBalancesContractClient(@Value("${network.arkane.vechain.deltabalances.contract-address}") String deltaBalancesContractAddress) {
        this.deltaBalancesContractAddress = deltaBalancesContractAddress;
        abiDefinition = createAbiDefinition();
    }

    private AbiDefinition createAbiDefinition() {
        AbiDefinition abiDefinition = new AbiDefinition();
        abiDefinition.setConstant(true);
        abiDefinition.setInputs(Arrays.asList(
                new AbiDefinition.NamedType("user", "address", false),
                new AbiDefinition.NamedType("tokens", "address[]", false)
                                             ));
        abiDefinition.setName("tokenBalances");
        abiDefinition.setOutputs(Collections.singletonList(new AbiDefinition.NamedType("", "uint256[]", false)));
        abiDefinition.setType("function");
        abiDefinition.setPayable(false);
        abiDefinition.setStateMutability("view");
        return abiDefinition;
    }

    public List<BigInteger> getVip180Balances(Address address, List<String> tokenAddresses)
            throws IOException {
        String[] addresses = tokenAddresses.toArray(new String[0]);
        ContractCall call = AbstractContract.buildCall(abiDefinition, address.toHexString(Prefix.ZeroLowerX), addresses);
        ContractCallResult contractCallResult = callContract(call, Address.fromHexString(deltaBalancesContractAddress), Revision.BEST);

        if (contractCallResult == null) {
            return null;
        }
        return decodeList(contractCallResult, new TypeReference<DynamicArray<Uint256>>() {}).stream()
                                                                                            .map(NumericType::getValue)
                                                                                            .collect(Collectors.toList());
    }

    public static <T extends Type> List<T> decodeList(final ContractCallResult contractCallResult,
                                                      final TypeReference<DynamicArray<T>> typeReference) {
        if (contractCallResult == null) {
            return null;
        }
        final Type type = FunctionReturnDecoder.decode(contractCallResult.getData(),
                                                       Utils.convert(Collections.singletonList(typeReference))).get(0);
        try {
            return typeReference.getClassType().cast(type).getValue();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
