package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TxInput {

    @JsonProperty("prev_hash")
    private String prevHash;

    private List<String> addresses;

}
