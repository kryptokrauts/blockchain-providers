package network.arkane.blockchainproviders.evmscan;

import network.arkane.blockchainproviders.evmscan.dto.EvmScanApiResponse;
import network.arkane.blockchainproviders.evmscan.dto.EvmTransaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface EvmScanClient {

    @GetMapping(value = "?module=account&action=txlist&sort=desc")
    EvmScanApiResponse<EvmTransaction> getTransactionList(@RequestParam String address,
                                                          @RequestParam Long page,
                                                          @RequestParam Long offset);

}
