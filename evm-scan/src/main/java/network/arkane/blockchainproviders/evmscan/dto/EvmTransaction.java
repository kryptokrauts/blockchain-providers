package network.arkane.blockchainproviders.evmscan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvmTransaction {
    private String blockNumber;
    private LocalDateTime timestamp;
    private String hash;
    private String blockHash;
    private String from;
    private String to;
    private String value;
    private String isError;
}
