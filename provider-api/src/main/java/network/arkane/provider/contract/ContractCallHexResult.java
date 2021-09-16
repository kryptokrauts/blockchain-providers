package network.arkane.provider.contract;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCallHexResult extends ContractCallResultType<String> {

    @Builder
    public ContractCallHexResult(String type,
                                 String value) {
        super(type, value);
    }

}
