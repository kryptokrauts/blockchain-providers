package network.arkane.provider.blockcypher;

import feign.Headers;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "blockcypher-client", url = "https://api.blockcypher.com/v1", decode404 = true)
public interface BlockcypherClient {

    @GetMapping("/{coin}/{chain}/addrs/{address}?unspentOnly=true&token={token}&includeScript=true")
    String getUnspents(@RequestHeader("User-Agent") String userAgent,
                       @PathVariable("coin") final String coin,
                       @PathVariable("chain") final String chain,
                       @RequestParam("token") final String token,
                       @PathVariable("address") final String address);

    @GetMapping("/{coin}/{chain}/addrs/{address}/balance?token={token}")
    String getBalance(@RequestHeader("User-Agent") String userAgent,
                      @PathVariable("coin") final String coin,
                      @PathVariable("chain") final String chain,
                      @RequestParam("token") final String token,
                      @PathVariable("address") final String address);

    @PostMapping("/{coin}/{chain}/txs/push?token={token}")
    @Headers("Content-Type: application/json")
    String sendSignedTransaction(@RequestHeader("User-Agent") String userAgent,
                                 @PathVariable("coin") final String coin,
                                 @PathVariable("chain") final String chain,
                                 @RequestParam("token") final String token,
                                 @RequestBody BlockCypherRawTransactionRequest request);

    @GetMapping("/{coin}/{chain}/addrs/{address}/full?token={token}")
    String getFullAddress(@RequestHeader("User-Agent") String userAgent,
                          @PathVariable("coin") final String coin,
                          @PathVariable("chain") final String chain,
                          @RequestParam("token") final String token,
                          @PathVariable("address") final String address);

    @GetMapping("/{coin}/{chain}/txs/{hash}?token={token}")
    String getTxByHash(@RequestHeader("User-Agent") String userAgent,
                       @PathVariable("coin") final String coin,
                       @PathVariable("chain") final String chain,
                       @RequestParam("token") final String token,
                       @PathVariable("hash") final String hash);
}
