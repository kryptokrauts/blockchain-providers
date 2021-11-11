package network.arkane.blockchainproviders.evmscan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvmAccount {
    private String address;
    private List<EvmTransaction> transactions;
}
