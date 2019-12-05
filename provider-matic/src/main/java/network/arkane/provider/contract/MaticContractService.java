package network.arkane.provider.contract;

import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.IOException;
import java.util.List;

@Component
public class MaticContractService extends EvmContractService implements ContractService {

    private MaticWeb3JGateway maticWeb3jGateway;

    public MaticContractService(MaticWeb3JGateway maticWeb3jGateway) {
        this.maticWeb3jGateway = maticWeb3jGateway;
    }


    @Override
    List<Type> executeContractCall(String from,
                                   String to,
                                   Function function) {
        String data = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall;
        try {
            ethCall = maticWeb3jGateway.web3().ethCall(
                    Transaction.createEthCallTransaction(from, to, data),
                    DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String value = ethCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }
}
