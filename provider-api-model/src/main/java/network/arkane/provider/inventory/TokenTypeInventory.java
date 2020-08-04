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
public class TokenTypeInventory {
    private Boolean fungible;
    private Long tokenTypeId;
    private Long balance;
    private List<String> tokenIds = new ArrayList<>();
}
