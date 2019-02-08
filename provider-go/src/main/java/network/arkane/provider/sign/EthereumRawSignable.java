package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class EthereumRawSignable implements Signable {
    private String data;

    @Builder
    public EthereumRawSignable(String data) {
        this.data = data;
    }
}
