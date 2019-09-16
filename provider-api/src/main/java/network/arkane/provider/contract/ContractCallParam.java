package network.arkane.provider.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractCallParam {
    private String type;
    private String value;
}
