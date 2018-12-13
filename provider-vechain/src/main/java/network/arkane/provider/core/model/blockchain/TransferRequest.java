package network.arkane.provider.core.model.blockchain;

import java.io.Serializable;

public class TransferRequest implements Serializable {
    private String raw;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
