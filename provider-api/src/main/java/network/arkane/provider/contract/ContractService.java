package network.arkane.provider.contract;

import java.util.List;

public interface ContractService {

    default List<String> callFunction(ContractCall contractCall) {
        throw new RuntimeException("Not implemented");
    }
}
