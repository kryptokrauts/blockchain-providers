package network.arkane.provider.nonfungible;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.ContractCall;
import network.arkane.provider.contract.ContractCallParam;
import network.arkane.provider.contract.ContractCallResultParam;
import network.arkane.provider.contract.EvmContractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class EvmMetaDataParser {

    private final RestTemplate restTemplate;
    private final EvmContractService contractService;
    private final MetaDataParser metaDataParser;

    public EvmMetaDataParser(
            final EvmContractService contractService,
            final Optional<CacheManager> cacheManager) {
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(250, ChronoUnit.MILLIS))
                .setReadTimeout(Duration.of(2, ChronoUnit.SECONDS))
                .build();
        this.contractService = contractService;
        metaDataParser = new MetaDataParser(cacheManager);
    }


    public NonFungibleMetaData parseMetaData(SecretType secretType,
                                             String tokenId,
                                             String contractType,
                                             String contractAddress) {

        return parseMetaData(secretType, tokenId, contractType, contractAddress, () -> {
            ContractCall metadataCall = contractType.endsWith("721")
                                        ? createErc721UriCall(contractAddress, tokenId)
                                        : createErc1155UriCall(contractAddress, tokenId);

            List<String> metaDataCallResult = contractService.callFunction(metadataCall).stream()
                                                             .map(type -> type.asStringType()
                                                                              .getValue())
                                                             .collect(Collectors.toList());
            if (metaDataCallResult.size() > 0 && StringUtils.isNotBlank(metaDataCallResult.get(0))) {
                if (isHttp(metaDataCallResult)) {
                    try {
                        return restTemplate.getForObject(metaDataCallResult.get(0), String.class, tokenId);
                    } catch (RestClientException e) {
                        log.error("Error parsing metadata", e);
                        return "";
                    }
                }
            }
            return "";
        });
    }

    @SneakyThrows
    public NonFungibleMetaData parseMetaData(SecretType secretType,
                                             String tokenId,
                                             String contractType,
                                             String contractAddress,
                                             String metaData) {

        return parseMetaData(secretType, tokenId, contractType, contractAddress, () -> metaData);
    }

    @SneakyThrows
    private NonFungibleMetaData parseMetaData(SecretType secretType,
                                              String tokenId,
                                              String contractType,
                                              String contractAddress,
                                              Supplier<String> metaDataSupplier) {
        return metaDataParser.parseMetaData(secretType, tokenId, contractType, contractAddress, metaDataSupplier.get());
    }

    private boolean isHttp(List<String> metaDataCallResult) {
        return metaDataCallResult.get(0).startsWith("http");
    }

    private ContractCall createErc721UriCall(String contractAddress,
                                             String tokenId) {
        return ContractCall.builder()
                           .contractAddress(contractAddress)
                           .functionName("tokenURI")
                           .inputs(Arrays.asList(ContractCallParam.builder().type("uint256").value(tokenId).build()))
                           .outputs(Collections.singletonList(ContractCallResultParam.builder().type("string").build()))
                           .build();
    }

    private ContractCall createErc1155UriCall(String contractAddress,
                                              String tokenId) {
        return ContractCall.builder()
                           .contractAddress(contractAddress)
                           .functionName("uri")
                           .inputs(Arrays.asList(ContractCallParam.builder().type("uint256").value(tokenId).build()))
                           .outputs(Collections.singletonList(ContractCallResultParam.builder().type("string").build()))
                           .build();
    }


}
