package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

import java.util.List;

@Data
@Builder
public class TokenAssociationSignable implements Signable {
    private String accountId;
    private List<String> tokenIds;
    private String transactionMemo;
}


