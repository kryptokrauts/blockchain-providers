package network.arkane.provider.contract;

import network.arkane.provider.chain.SecretType;

import java.util.List;

public interface ContractService {

    SecretType type();

    default List<Object> callFunction(ContractCall contractCall) {
        throw new RuntimeException("Not implemented");
    }

}
