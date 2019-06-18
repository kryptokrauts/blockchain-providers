package network.arkane.provider.opensea;

import network.arkane.provider.opensea.domain.Assets;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "opensea-client", url = "https://api.opensea.io/api/v1", decode404 = true, configuration = {OpenSeaClientConfiguration.class})
public interface OpenSeaClient {

    @RequestMapping(method = RequestMethod.GET, value = "/assets")
    Assets listAssets(@RequestParam("owner") final String owner, @RequestParam(value = "asset_contract_addresses") final List<String> contractAddresses);
}
