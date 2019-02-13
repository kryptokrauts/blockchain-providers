package network.arkane.provider.litecoin.wallet.exporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class ExportedLitecoinKey {

    private String type;
    private String value;

    @Builder
    public ExportedLitecoinKey(@JsonProperty("type") String type, @JsonProperty("value") String value) {
        this.type = type;
        this.value = value;
    }
}
