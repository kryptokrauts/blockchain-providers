package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

@Data
@Builder
public class HederaRawSignable implements Signable {

    private String data;

}
