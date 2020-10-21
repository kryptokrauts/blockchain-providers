package network.arkane.provider.contract;

import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EvmContractService implements ContractService {

    private EvmWeb3jGateway evmWeb3jGateway;

    public EvmContractService(EvmWeb3jGateway evmWeb3jGateway) {
        this.evmWeb3jGateway = evmWeb3jGateway;
    }

    public List<String> callFunction(ContractCall contractCall) {
        Function f = createFunction(contractCall);
        try {
            return executeContractCall(contractCall, f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private List<String> executeContractCall(ContractCall contractCall, Function f) throws Exception {


        if (contractCall.getOutputs().size() <= 1) {
            Type result = executeRemoteCallSingleValueReturn(f, contractCall).send();
            return result == null ? Collections.emptyList() : Collections.singletonList(result.getValue().toString());
        } else {
            List<Type> result = executeRemoteCallMultipleValueReturn(f, contractCall).send();
            return result == null
                   ? Collections.emptyList()
                   : result.stream().map(Type::getValue).map(Object::toString).collect(Collectors.toList());
        }
    }

    private Function createFunction(ContractCall contractCall) {
        return new Function(contractCall.getFunctionName(),
                            contractCall.getInputs().stream().map(i -> AbiTypesFactory.getType(i.getType(), i.getValue())).collect(Collectors.toList()),
                            contractCall.getOutputs().stream().map(o -> AbiTypeReferences.getType(o.getType())).collect(Collectors.toList()));
    }

    private <T extends Type> RemoteCall<T> executeRemoteCallSingleValueReturn(Function function,
                                                                              ContractCall contractCall) {
        return new RemoteCall<>(() -> executeCallSingleValueReturn(function, contractCall));
    }

    public List<Type> executeContractCall(String from,
                                          String to,
                                          Function function) {
        String data = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = null;
        try {
            ethCall = evmWeb3jGateway.web3().ethCall(
                    Transaction.createEthCallTransaction(from, to, data),
                    DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String value = ethCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }


    private String getCaller(ContractCall contractCall) {
        return contractCall.getCaller() != null && contractCall.getCaller().length() == 42 && contractCall.getCaller().startsWith("0x")
               ? contractCall.getCaller()
               : "0x0000000000000000000000000000000000000000";
    }

    @SuppressWarnings("unchecked")
    private <T extends Type> T executeCallSingleValueReturn(Function function,
                                                            ContractCall contractCall) throws IOException {
        List<Type> values = executeContractCall(getCaller(contractCall), contractCall.getContractAddress(), function);
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }


    protected List<Type> executeCallMultipleValueReturn(Function function, ContractCall contractCall) throws IOException {
        return executeContractCall(getCaller(contractCall), contractCall.getContractAddress(), function);
    }

    protected RemoteCall<List<Type>> executeRemoteCallMultipleValueReturn(Function function, ContractCall contractCall) {
        return new RemoteCall<>(() -> executeCallMultipleValueReturn(function, contractCall));
    }
}
