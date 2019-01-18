package network.arkane.provider.sochain;

import feign.Headers;
import network.arkane.provider.sochain.domain.BalanceResult;
import network.arkane.provider.sochain.domain.SendSignedTransactionRequest;
import network.arkane.provider.sochain.domain.SendSignedTransactionResult;
import network.arkane.provider.sochain.domain.SoChainResult;
import network.arkane.provider.sochain.domain.Transactions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "chainso-client", url = "https://chain.so/api/v2", decode404 = true)
public interface SoChainClient {

    @RequestMapping(method = RequestMethod.GET, value = "/get_address_balance/{network}/{address}")
    SoChainResult<BalanceResult> getBalance(@RequestHeader("User-Agent") String userAgent,
                                            @PathVariable("network") String network,
                                            @PathVariable("address") String address);

    @RequestMapping(method = RequestMethod.GET, value = "/get_tx_unspent/{network}/{address}")
    SoChainResult<Transactions> getUnspentTransactions(@RequestHeader("User-Agent") String userAgent,
                                                       @PathVariable("network") String network,
                                                       @PathVariable("address") String address);

    @RequestMapping(method = RequestMethod.GET, value = "/get_tx_received/{network}/{address}")
    SoChainResult<Transactions> getReceivedTransactions(@RequestHeader("User-Agent") String userAgent,
                                                        @PathVariable("network") String network,
                                                        @PathVariable("address") String address);

    @RequestMapping(method = RequestMethod.GET, value = "/get_tx_spent/{network}/{address}")
    SoChainResult<Transactions> getSpentTransactions(@RequestHeader("User-Agent") String userAgent,
                                                     @PathVariable("network") String network,
                                                     @PathVariable("address") String address);

    @RequestMapping(method = RequestMethod.POST, value = "/send_tx/{network}")
    @Headers("Content-Type: application/json")
    SoChainResult<SendSignedTransactionResult> sendSignedTransaction(@RequestHeader("User-Agent") String userAgent,
                                                                     @PathVariable("network") String network,
                                                                     @RequestBody SendSignedTransactionRequest request);

}
