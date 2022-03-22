package network.arkane.provider.hedera.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.hedera.token.HederaCustomFee;
import network.arkane.provider.hedera.token.HederaTokenSupplyType;
import network.arkane.provider.hedera.token.HederaTokenType;
import network.arkane.provider.sign.domain.Signable;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HederaTokenCreationSignable implements Signable {
    private List<HederaCustomFee> customFees = new ArrayList<>();

    private String accountId;

    private String treasuryAccountId;

    private String tokenName = "";

    private String tokenSymbol = "";

    private int decimals = 0;

    private long initialSupply = 0L;

    private boolean freezeDefault = false;

    private ZonedDateTime expirationTime = null;

    private Duration autoRenewPeriod = null;

    private String tokenMemo = "";

    private HederaTokenType tokenType;

    private HederaTokenSupplyType supplyType;

    private long maxSupply;
}
