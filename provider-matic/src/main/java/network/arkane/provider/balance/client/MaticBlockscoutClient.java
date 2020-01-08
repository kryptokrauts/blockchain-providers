package network.arkane.provider.balance.client;

import network.arkane.provider.balance.MaticBlockscoutResponse;
import network.arkane.provider.balance.MaticBlockscoutTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${feign.client.matic-blockscout.scheme}://${feign.client.matic-blockscout.host}",
             name = "matic-blockscout-client",
             configuration = MaticBlockscoutClientConfiguration.class)
public interface MaticBlockscoutClient {

    @GetMapping("?module=account&action=tokenlist&address={walletAddress}")
    MaticBlockscoutResponse<List<MaticBlockscoutTokenResponse>> getTokensForAddress(@RequestParam("walletAddress") final String walletAddress);
}
