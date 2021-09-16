package network.arkane.provider.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ContractCallResultType<T> {

    private String type;

    private T value;


    public Optional<ContractCallStringResult> asStringType() {
        if (this instanceof ContractCallStringResult) {
            return Optional.of((ContractCallStringResult) this);
        }
        return Optional.empty();
    }

    public Optional<ContractCallNumericResult> asNumericType() {
        if (this instanceof ContractCallNumericResult) {
            return Optional.of((ContractCallNumericResult) this);
        }
        return Optional.empty();
    }

    public Optional<ContractCallBooleanResult> asBooleanType() {
        if (this instanceof ContractCallBooleanResult) {
            return Optional.of((ContractCallBooleanResult) this);
        }
        return Optional.empty();
    }

    public Optional<ContractCallHexResult> asHexType() {
        if (this instanceof ContractCallHexResult) {
            return Optional.of((ContractCallHexResult) this);
        }
        return Optional.empty();
    }
}
