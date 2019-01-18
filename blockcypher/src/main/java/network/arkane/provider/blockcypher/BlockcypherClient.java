package network.arkane.provider.blockcypher;

import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "blockcypher-client", url = "https://api.blockcypher.com/v1", decode404 = true)
public interface BlockcypherClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{coin}/{network}/addr/{address}?unspentOnly=true")
    BlockcypherAddressUnspents getUnspents(@RequestHeader("User-Agent") String userAgent,
                                           @PathVariable("network") final String network,
                                           @PathVariable("coin") final String coin,
                                           @PathVariable("address") final String address);

    @RequestMapping(method = RequestMethod.GET, value = "/{coin}/{network}/addr/{address}/balance")
    BlockcypherAddress getBalance(@RequestHeader("User-Agent") String userAgent,
                                  @PathVariable("network") final String network,
                                  @PathVariable("coin") final String coin,
                                  @PathVariable("address") final String address);
}
