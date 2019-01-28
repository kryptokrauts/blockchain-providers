package network.arkane.provider.bitcoin.wallet.exporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class ExportedBitcoinKey {
    private String value;
    private String type;

    @Builder
    public ExportedBitcoinKey(final @JsonProperty("value") String value, @JsonProperty("type") final String type) {
        this.value = value;
        this.type = type;
    }
}
