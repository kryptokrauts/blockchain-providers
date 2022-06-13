package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class ImxRawSignable implements Signable {
    private String data;

    @Builder(builderClassName = "ImxRawSignableBuilder")
    public ImxRawSignable(final String data) {
        this.data = data;
    }
}
