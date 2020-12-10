package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.ContractCall;
import network.arkane.provider.contract.ContractCallParam;
import network.arkane.provider.contract.ContractCallResultParam;
import network.arkane.provider.contract.EvmContractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MetaDataParser {

    private ObjectMapper objectMapper;
    private EvmContractService contractService;
    private Optional<CacheManager> cacheManager;

    public MetaDataParser(
            final EvmContractService contractService,
            final Optional<CacheManager> cacheManager) {
        this.cacheManager = cacheManager;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.contractService = contractService;
    }


    @SneakyThrows
    public NonFungibleMetaData parseMetaData(SecretType secretType,
                                             String tokenId,
                                             String contractType,
                                             String contractAddress) {

        String cacheKey = secretType + "-" + tokenId + "-" + contractType + "-" + contractAddress;
        if (this.cacheManager.isPresent()) {
            Cache cache = cacheManager.get().getCache("non-fungibles-meta-data");
            NonFungibleMetaData cachedResult = cache.get(cacheKey, NonFungibleMetaData.class);
            if (cachedResult != null) {
                return cachedResult;
            }
        }

        NonFungibleMetaData result = null;

        try {
            ContractCall metadataCall = contractType.endsWith("721")
                                        ? createErc721UriCall(contractAddress, tokenId)
                                        : createErc1155UriCall(contractAddress, tokenId);

            List<String> metaDataCallResult = contractService.callFunction(metadataCall);
            if (metaDataCallResult.size() > 0 && StringUtils.isNotBlank(metaDataCallResult.get(0))) {
                if (isHttp(metaDataCallResult)) {
                    try {
                        result = parseMetaData(objectMapper.readValue(new URL(metaDataCallResult.get(0)), JsonNode.class));
                    } catch (IOException e) {
                        if (e.getMessage().contains("500") && e.getMessage().contains("api.opensea.io")) {
                            String url = metaDataCallResult.get(0);
                            url = url.replace("https://api.opensea.io/", "https://rinkeby-api.opensea.io/");
                            result = parseMetaData(objectMapper.readValue(new URL(url), JsonNode.class));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting metadata for nonfungible", e);
        }
        if (this.cacheManager.isPresent()) {
            Cache cache = cacheManager.get().getCache("non-fungibles-meta-data");
            cache.put(cacheKey, result);
        }
        return result;
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

    @SneakyThrows
    public NonFungibleMetaData parseMetaData(JsonNode metaData) {
        return NonFungibleMetaData.builder().json(metaData).objectMapper(objectMapper).build();

    }

}
