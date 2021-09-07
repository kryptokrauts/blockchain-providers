package network.arkane.blockchainproviders.covalent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CovalentTxHistoryResponse {

    private String address;

    @JsonProperty("updated_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    @JsonProperty("next_update_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime nextUpdateAt;

    @JsonProperty("quote_currency")
    private String quoteCurrency;

    @JsonProperty("chain_id")
    private Long chainId;

    private List<CovalentTxHistoryItem> items;

    public String getChain() {
        return CovalentChain.withId(chainId)
                            .getSecretType()
                            .name();
    }

}
