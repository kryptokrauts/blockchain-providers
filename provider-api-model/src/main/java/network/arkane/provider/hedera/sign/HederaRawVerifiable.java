package network.arkane.provider.hedera.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Verifiable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HederaRawVerifiable implements Verifiable {
    private String signature;

    private String message;

    private String address;
}
