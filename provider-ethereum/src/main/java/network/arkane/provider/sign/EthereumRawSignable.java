package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class EthereumRawSignable implements Signable {
    private String data;
    @Builder.Default
    private boolean prefix = true;
    @Builder.Default
    private boolean hash = true;

    @Builder
    public EthereumRawSignable(final String data,
                               final boolean prefix,
                               final boolean hash) {
        this.data = data;
        this.prefix = prefix;
        this.hash = hash;
    }
}
