package network.arkane.provider.contract;

import network.arkane.provider.chain.SecretType;

import java.util.List;

public interface ContractService {

    SecretType type();

    default List<String> callFunction(ContractCall contractCall) {
        throw new RuntimeException("Not implemented");
    }

    default List<Object> read(ContractCall contractCall) {
        throw new RuntimeException("Not implemented");
    }
}
