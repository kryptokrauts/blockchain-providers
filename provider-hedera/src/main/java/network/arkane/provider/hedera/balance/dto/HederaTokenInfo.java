package network.arkane.provider.hedera.balance.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class HederaTokenInfo {
    private String symbol;
    private String name;
    private String type;
    private int decimals;
    private String tokenMemo;
}
