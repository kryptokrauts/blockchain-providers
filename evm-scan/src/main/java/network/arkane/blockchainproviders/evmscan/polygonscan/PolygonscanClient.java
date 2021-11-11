package network.arkane.blockchainproviders.evmscan.polygonscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "polygonscan-client", url = "${polygonscan.api-base-url}", decode404 = true, configuration = {PolygonscanClientConfiguration.class})
public interface PolygonscanClient extends EvmScanClient {

}
