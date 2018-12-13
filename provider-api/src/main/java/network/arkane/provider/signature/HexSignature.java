package network.arkane.provider.signature;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;

@Data
@NoArgsConstructor
public class HexSignature extends Signature {

    private String r;
    private String s;
    private int v;

    @Builder
    public HexSignature(final byte[] r,
                        final byte[] s,
                        final int v) {
        this.r = Hex.toHexString(r);
        this.s = Hex.toHexString(s);
        this.v = v;
    }
}
