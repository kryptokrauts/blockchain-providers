package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigDecimal;

@Data
@Builder
public class HbarTransferSignable implements Signable {
    private String from;
    private String to;
    private BigDecimal amount;
}

