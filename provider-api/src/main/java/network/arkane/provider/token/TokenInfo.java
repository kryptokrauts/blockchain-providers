package network.arkane.provider.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfo {
    private String address;
    private String name;
    private String symbol;
    private Integer decimals;
    private String type;
    private String logo;
    private boolean transferable = true;
}
