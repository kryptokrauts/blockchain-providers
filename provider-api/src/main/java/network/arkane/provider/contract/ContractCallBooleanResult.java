package network.arkane.provider.contract;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCallBooleanResult extends ContractCallResultType<Boolean> {

    @Builder
    public ContractCallBooleanResult(String type,
                                     Boolean value) {
        super(type, value);
    }

}
