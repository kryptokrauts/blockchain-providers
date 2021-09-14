package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigInteger;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockcypherBlockchain {
    private String name;
    private BigInteger height;
    private String hash;
}
