package network.arkane.provider.tron.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TronCallValueInfo {
    private Long callValue;
    private Object tokenId;
}
