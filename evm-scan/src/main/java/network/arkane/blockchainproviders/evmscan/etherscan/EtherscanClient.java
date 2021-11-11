package network.arkane.blockchainproviders.evmscan.etherscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "etherscan-client", url = "${etherscan.api-base-url}", decode404 = true, configuration = {EtherscanClientConfiguration.class})
public interface EtherscanClient extends EvmScanClient {

}
