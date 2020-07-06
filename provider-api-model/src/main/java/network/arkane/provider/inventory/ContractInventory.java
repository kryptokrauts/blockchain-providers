package network.arkane.provider.inventory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class ContractInventory {
    private String contractAddress;
    @Builder.Default
    private List<TokenTypeInventory> tokenTypes = new ArrayList<>();
}
