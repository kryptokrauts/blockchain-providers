package network.arkane.provider.hedera.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.balance.HederaTokenInfoService;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Optional;

import static network.arkane.provider.hedera.nonfungible.HederaNonfungibleGateway.NON_FUNGIBLE_UNIQUE;

@Component
@Slf4j
public class HederaNftContractInfoService {
    private final HederaTokenInfoService hederaTokenInfoService;
    private final RestTemplate restTemplate;

    public HederaNftContractInfoService(HederaTokenInfoService hederaTokenInfoService) {
        this.hederaTokenInfoService = hederaTokenInfoService;
        restTemplate = new RestTemplateBuilder().defaultMessageConverters()
                                                .setConnectTimeout(Duration.ofSeconds(5))
                                                .additionalInterceptors((request, body, execution) -> {
                                                    request.getHeaders().set("User-Agent", "curl/7.54.0/venly.hederanftcontractinfo");
                                                    return execution.execute(request, body);
                                                })
                                                .setReadTimeout(Duration.ofSeconds(20))
                                                .build();
    }


    @Cacheable(value = "hedera-contract-info", unless = "#result == null")
    public Optional<NonFungibleContract> getContractInfo(String tokenId) {
        return hederaTokenInfoService.getTokenInfo(tokenId)
                                     .filter(ti -> NON_FUNGIBLE_UNIQUE.equalsIgnoreCase(ti.getType()))
                                     .map(ti -> {
                                         if (ti.getSymbol().startsWith("http")) {
                                             try {
                                                 return restTemplate.getForObject(ti.getSymbol(), NonFungibleContract.class);
                                             } catch (Exception e) {
                                                 //auto parsing failed
                                                 log.debug("Erorr parsing nft contract", e);
                                             }

                                         }
                                         return NonFungibleContract.builder()
                                                                   .name(ti.getName())
                                                                   .type(NON_FUNGIBLE_UNIQUE)
                                                                   .address(ti.getAddress())
                                                                   .symbol(ti.getSymbol())
                                                                   .imageUrl(ti.getLogo())
                                                                   .build();
                                     });
    }


}
