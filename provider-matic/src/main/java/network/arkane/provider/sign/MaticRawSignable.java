package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class MaticRawSignable implements Signable {
    private String data;
    private boolean prefix = true;
    private boolean hash = true;

    @Builder(builderClassName = "MaticRawSignableBuilder")
    public MaticRawSignable(final String data,
                            final boolean prefix,
                            final boolean hash) {
        this.data = data;
        this.prefix = prefix;
        this.hash = hash;
    }

    public static class MaticRawSignableBuilder {
        private boolean hash = true;
        private boolean prefix = true;
    }
}
