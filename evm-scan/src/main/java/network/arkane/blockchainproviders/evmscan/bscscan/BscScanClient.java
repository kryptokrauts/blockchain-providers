package network.arkane.blockchainproviders.evmscan.bscscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bscscan-client", url = "${bscscan.api-base-url}", decode404 = true, configuration = {BscScanClientConfiguration.class})
public interface BscScanClient extends EvmScanClient {

}
