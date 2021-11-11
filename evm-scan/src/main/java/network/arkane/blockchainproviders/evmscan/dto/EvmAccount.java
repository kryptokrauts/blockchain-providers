package network.arkane.blockchainproviders.evmscan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvmAccount {
    private String address;
    private SecretType chain;
    private List<EvmTransaction> transactions;
}
