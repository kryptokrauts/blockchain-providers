package network.arkane.provider.opensea;

import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.Assets;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "opensea-client", url = "${opensea.api-base-url}", decode404 = true, configuration = {OpenSeaClientConfiguration.class})
public interface OpenSeaClient {

    @RequestMapping(method = RequestMethod.GET, value = "/assets")
    Assets listAssets(@RequestParam("owner") final String owner, @RequestParam(value = "asset_contract_addresses") final List<String> contractAddresses);

    @RequestMapping(method = RequestMethod.GET, value = "/asset/{asset_contract_address}/{token_id}")
    Asset getAsset(@PathVariable(value = "asset_contract_address") final String contractAddress,
                   @PathVariable(value = "token_id") final String tokenId);
}
