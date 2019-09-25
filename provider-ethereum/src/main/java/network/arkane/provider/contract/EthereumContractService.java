package network.arkane.provider.contract;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
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
public class EthereumContractService extends EvmContractService implements ContractService {

    private EthereumWeb3JGateway ethereumWeb3JGateway;

    public EthereumContractService(EthereumWeb3JGateway ethereumWeb3JGateway) {
        this.ethereumWeb3JGateway = ethereumWeb3JGateway;
    }


    @Override
    List<Type> executeContractCall(String from, String to, Function function) {
        String data = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = null;
        try {
            ethCall = ethereumWeb3JGateway.web3().ethCall(
                    Transaction.createEthCallTransaction(from, to, data),
                    DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String value = ethCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }


}
