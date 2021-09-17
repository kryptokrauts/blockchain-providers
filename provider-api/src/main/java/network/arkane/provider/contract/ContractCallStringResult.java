package network.arkane.provider.contract;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCallStringResult extends ContractCallResultType<String> {

    @Builder
    public ContractCallStringResult(String type,
                                    String value) {
        super(type, value);
    }
}


