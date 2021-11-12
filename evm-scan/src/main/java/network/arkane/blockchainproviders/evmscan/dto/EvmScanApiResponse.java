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
public class EvmScanApiResponse<T> {

    private String status;

    private String message;

    private List<T> result;

}
