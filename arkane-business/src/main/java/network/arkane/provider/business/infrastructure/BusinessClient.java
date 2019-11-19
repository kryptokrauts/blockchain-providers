package network.arkane.provider.business.infrastructure;

import network.arkane.provider.business.token.model.TokenContract;
import network.arkane.provider.business.token.model.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;
import java.util.List;

@FeignClient(url = "${feign.client.business.scheme}://${feign.client.business.host}",
             name = "business-client",
             configuration = BusinessClientConfiguration.class)
public interface BusinessClient {

    @GetMapping("/api/{userAddress}/items")
    List<TokenDto> getTokensForAddress(@PathVariable("userAddress") final String userAddress);

    @GetMapping("/api/contracts/{contractAddress}")
    TokenContract getContract(@PathVariable("contractAddress") final String contractAddress);

    @GetMapping("/api//contracts/{contractAddress}/token/{tokenId}")
    TokenDto getToken(@PathVariable("contractAddress") final String contractAddress,
                      @PathVariable("tokenId") final BigInteger tokenId);
}
