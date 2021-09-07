package network.arkane.blockchainproviders.covalent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CovalentTxHistoryItem {

    @JsonProperty("block_signed_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime blockSignedAt;

    @JsonProperty("block_height")
    private Integer blockHeight;

    @JsonProperty("tx_hash")
    private String txHash;

    @JsonProperty("tx_offset")
    private Integer txOffset;

    @JsonProperty("successful")
    private Boolean successful;

    @JsonProperty("from_address")
    private String fromAddress;

    @JsonProperty("from_address_label")
    private String fromAddressLabel;

    @JsonProperty("to_address")
    private String toAddress;

    @JsonProperty("to_address_label")
    private String toAddressLabel;

    private BigDecimal value;

    @JsonProperty("value_quote")
    private Double valueQuote;

    @JsonProperty("gas_offered")
    private Long gasOffered;

    @JsonProperty("gas_spent")
    private Long gasSpent;

    @JsonProperty("gas_price")
    private Long gasPrice;

    @JsonProperty("gas_quote")
    private Long gasQuote;

    @JsonProperty("gas_quote_rate")
    private Long gasQuoteRate;
}
