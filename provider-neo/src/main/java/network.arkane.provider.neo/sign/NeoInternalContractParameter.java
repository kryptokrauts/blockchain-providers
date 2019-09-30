package network.arkane.provider.neo.sign;

import io.neow3j.contract.ContractParameter;
import io.neow3j.model.types.ContractParameterType;

public class NeoInternalContractParameter extends ContractParameter {

    public NeoInternalContractParameter(String name, ContractParameterType paramType, Object value) {
        super(name, paramType, value);
    }
}
