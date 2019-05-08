package network.arkane.provider.tron.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class TrxRawSignable implements Signable {
    private String data;

    @Builder
    public TrxRawSignable(String data) {
        this.data = data;
    }
}
