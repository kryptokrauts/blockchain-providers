package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TX {
    private String hash;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime confirmed;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime received;

    private List<String> addresses;

    private List<TxInput> inputs;

    private List<TxOutput> outputs;
}
