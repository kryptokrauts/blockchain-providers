package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import network.arkane.provider.contract.ContractCall;
import network.arkane.provider.contract.ContractCallParam;
import network.arkane.provider.contract.ContractCallResultParam;
import network.arkane.provider.contract.MaticContractService;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class MetaDataParser {

    private ObjectMapper objectMapper;
    private MaticContractService maticContractService;

    public MetaDataParser(ObjectMapper objectMapper,
                          MaticContractService maticContractService) {
        this.objectMapper = objectMapper;
        this.maticContractService = maticContractService;
    }


    @Cacheable(value = "non-fungibles-meta-data", key = "{#contract.address, #tokenId}")
    @SneakyThrows
    public NonFungibleMetaData parseMetaData(String tokenId,
                                             NonFungibleContract contract) {
        ContractCall metadataCall = contract.getType().equalsIgnoreCase("ERC-721")
                                    ? createErc721OwnerCall(contract, tokenId)
                                    : createErc1155OwnerCall(contract, tokenId);

        List<String> metaDataCallResult = maticContractService.callFunction(metadataCall);
        if (metaDataCallResult.size() > 0 && StringUtils.isNotBlank(metaDataCallResult.get(0))) {
            return parseMetaData(objectMapper.readValue(metaDataCallResult.get(0), JsonNode.class));
        }
        return null;
    }

    private ContractCall createErc721OwnerCall(NonFungibleContract contract,
                                               String tokenId) {
        return ContractCall.builder()
                           .contractAddress(contract.getAddress())
                           .functionName("tokenURI")
                           .inputs(Arrays.asList(ContractCallParam.builder().type("uint256").value(tokenId).build()))
                           .outputs(Collections.singletonList(ContractCallResultParam.builder().type("string").build()))
                           .build();
    }

    private ContractCall createErc1155OwnerCall(NonFungibleContract contract,
                                                String tokenId) {
        return ContractCall.builder()
                           .contractAddress(contract.getAddress())
                           .functionName("uri")
                           .inputs(Arrays.asList(ContractCallParam.builder().type("uint256").value(tokenId).build()))
                           .outputs(Collections.singletonList(ContractCallResultParam.builder().type("string").build()))
                           .build();
    }


    @SneakyThrows
    public NonFungibleMetaData parseMetaData(JsonNode metaData) {
        NonFungibleMetaData.NonFungibleMetaDataBuilder metaDataBuilder = NonFungibleMetaData.builder();
        if (metaData.has("title")) {
            metaDataBuilder.title(metaData.get("title").asText());
        }
        if (metaData.has("type")) {
            metaDataBuilder.type(metaData.get("type").asText());
        }
        if (metaData.has("type")) {
            metaDataBuilder.type(metaData.get("type").asText());
        }

        JsonNode properties = null;
        if (metaData.has("properties")) {
            properties = metaData.get("properties");
        } else if (metaData.has("attributes")) {
            properties = metaData.get("properties");
        }

        metaDataBuilder.properties(properties);

        return metaDataBuilder.build();
    }
}
