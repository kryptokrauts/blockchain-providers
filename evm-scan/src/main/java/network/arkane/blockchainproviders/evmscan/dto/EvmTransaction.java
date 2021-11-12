package network.arkane.blockchainproviders.evmscan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.blockchainproviders.evmscan.converter.EpochToLocalDateTimeConverter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvmTransaction {
    private String blockNumber;
    @JsonProperty("timeStamp")
    @JsonDeserialize(converter = EpochToLocalDateTimeConverter.class)
    private LocalDateTime timestamp;
    private String hash;
    private String blockHash;
    private String from;
    private String to;
    private String value;
    private String isError;
}
