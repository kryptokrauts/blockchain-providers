package network.arkane.provider.contract;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.core.model.blockchain.ContractCallResult;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Revision;
import network.arkane.provider.gateway.VechainGateway;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.io.IOException;
import java.util.List;

import static network.arkane.provider.clients.base.AbstractClient.callContract;

@Component
public class VechainContractService extends EvmContractService implements ContractService {

    private VechainGateway vechainGateway;

    public VechainContractService(VechainGateway vechainGateway) {
        super(null);
    }

    @Override
    public List<Type> executeContractCall(String from,
                                          String to,
                                          Function function) {
        String data = FunctionEncoder.encode(function);
        network.arkane.provider.core.model.blockchain.ContractCall contractCall = new network.arkane.provider.core.model.blockchain.ContractCall();
        contractCall.setCaller(from);
        contractCall.setData(data);
        contractCall.setValue("0x0");

        try {
            ContractCallResult result = callContract(contractCall, Address.fromHexString(to), Revision.BEST);

            return FunctionReturnDecoder.decode(result.getData(), function.getOutputParameters());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public SecretType type() {
        return SecretType.VECHAIN;
    }
}
