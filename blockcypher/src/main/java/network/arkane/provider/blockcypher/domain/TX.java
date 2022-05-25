package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TX {
    private String hash;

    @JsonProperty("block_hash")
    private String blockHash;

    @JsonProperty("block_height")
    private BigInteger blockHeight;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime confirmed;

    private BigInteger confirmations;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime received;

    private List<String> addresses;

    private List<TxInput> inputs;

    private List<TxOutput> outputs;
}
