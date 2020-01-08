package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class EthereumRawSignable implements Signable {
    private String data;
    private boolean prefix = true;
    private boolean hash = true;

    @Builder(builderClassName = "EthereumRawSignableBuilder")
    public EthereumRawSignable(final String data,
                               final boolean prefix,
                               final boolean hash) {
        this.data = data;
        this.prefix = prefix;
        this.hash = hash;
    }

    public static class EthereumRawSignableBuilder {
        private boolean hash = true;
        private boolean prefix = true;
    }
}
