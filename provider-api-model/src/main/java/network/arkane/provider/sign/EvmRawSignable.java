package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class EvmRawSignable implements Signable {
    private String data;
    private boolean prefix = true;
    private boolean hash = true;

    @Builder(builderClassName = "EvmRawSignableBuilder")
    public EvmRawSignable(final String data,
                          final boolean prefix,
                          final boolean hash) {
        this.data = data;
        this.prefix = prefix;
        this.hash = hash;
    }

}
