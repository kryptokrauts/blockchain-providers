package network.arkane.provider.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ContractCallResultType<T> {

    private String type;

    private T value;

    public ContractCallStringResult asStringType() {
        if (this instanceof ContractCallStringResult) {
            return (ContractCallStringResult) this;
        }
        throw new ClassCastException();
    }

    public ContractCallNumericResult asNumericType() {
        if (this instanceof ContractCallNumericResult) {
            return (ContractCallNumericResult) this;
        }
        throw new ClassCastException();
    }

    public ContractCallBooleanResult asBooleanType() {
        if (this instanceof ContractCallBooleanResult) {
            return (ContractCallBooleanResult) this;
        }
        throw new ClassCastException();
    }

    public ContractCallHexResult asHexType() {
        if (this instanceof ContractCallHexResult) {
            return (ContractCallHexResult) this;
        }
        throw new ClassCastException();
    }
}
