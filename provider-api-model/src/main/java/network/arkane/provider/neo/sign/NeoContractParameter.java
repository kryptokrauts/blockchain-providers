package network.arkane.provider.neo.sign;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NeoContractParameter {
    private String type;
    private String value;
}
