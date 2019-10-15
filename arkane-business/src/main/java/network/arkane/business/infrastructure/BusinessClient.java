package network.arkane.business.infrastructure;

import network.arkane.business.token.model.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${feign.client.business.scheme}://${feign.client.business.host}")
public interface BusinessClient {

    @GetMapping("/api/{userAddress}/items")
    List<TokenDto> getTokensForAddress(@PathVariable("userAddress") final String userAddress);

}
