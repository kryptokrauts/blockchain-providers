package network.arkane.provider.inventory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class TokenTypeInventory {
    private Boolean fungible;
    private String tokenTypeId;
    private BigInteger balance;
    private List<String> tokenIds = new ArrayList<>();
}
