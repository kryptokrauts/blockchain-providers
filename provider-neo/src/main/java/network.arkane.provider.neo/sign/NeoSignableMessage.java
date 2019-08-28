package network.arkane.provider.neo.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class NeoSignableMessage implements Signable {

    private String data;
    private boolean hash = true;

    @Builder(builderClassName = "NeoSignableMessageBuilder")
    public NeoSignableMessage(final String data,
                              final boolean hash) {
        this.data = data;
        this.hash = hash;
    }

    public static class NeoSignableMessageBuilder {
        private boolean hash = true;
    }
}