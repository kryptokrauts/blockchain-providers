package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.contract.ContractCall;
import network.arkane.provider.contract.ContractCallParam;
import network.arkane.provider.contract.ContractCallResultParam;
import network.arkane.provider.contract.MaticContractService;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class MetaDataParser {

    private ObjectMapper objectMapper;
    private MaticContractService maticContractService;

    public MetaDataParser(final MaticContractService maticContractService) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.maticContractService = maticContractService;
    }


    @Cacheable(value = "non-fungibles-meta-data", key = "{#contract.address, #tokenId}")
    @SneakyThrows
    public NonFungibleMetaData parseMetaData(String tokenId,
                                             NonFungibleContract contract) {
        try {
            ContractCall metadataCall = contract.getType().equalsIgnoreCase("ERC-721")
                                        ? createErc721UriCall(contract, tokenId)
                                        : createErc1155UriCall(contract, tokenId);

            List<String> metaDataCallResult = maticContractService.callFunction(metadataCall);
            if (metaDataCallResult.size() > 0 && StringUtils.isNotBlank(metaDataCallResult.get(0))) {
                return parseMetaData(objectMapper.readValue(new URL(metaDataCallResult.get(0)), JsonNode.class));
            }
        } catch (Exception e) {
            log.error("Error getting metadata for nonfungible", e);
            return null;
        }
        return null;
    }

    private ContractCall createErc721UriCall(NonFungibleContract contract,
                                             String tokenId) {
        return ContractCall.builder()
                           .contractAddress(contract.getAddress())
                           .functionName("tokenURI")
                           .inputs(Arrays.asList(ContractCallParam.builder().type("uint256").value(tokenId).build()))
                           .outputs(Collections.singletonList(ContractCallResultParam.builder().type("string").build()))
                           .build();
    }

    private ContractCall createErc1155UriCall(NonFungibleContract contract,
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

        ObjectNode properties = createPropertiesRootNode(metaData);

        if (metaData.has("imageUrl")) {
            properties.put("image", metaData.get("imageUrl").asText());
        }

        if (metaData.has("url")) {
            properties.put("url", metaData.get("url").asText());
        }

        addTokenTypeFieldsToProperties(metaData, properties);

        metaDataBuilder.properties(properties);

        return metaDataBuilder.build();
    }

    private void addTokenTypeFieldsToProperties(JsonNode metaData,
                                                ObjectNode properties) {
        if (metaData.has("tokenType")) {
            JsonNode tokenType = metaData.get("tokenType");
            properties.put("name", tokenType.get("name").asText(""));
            properties.put("description", tokenType.get("description").asText(""));
            properties.put("backgroundColor", tokenType.get("backgroundColor").asText(""));
            if (tokenType.hasNonNull("image")) {
                properties.put("image", tokenType.get("image").asText(""));
            }
        }
    }

    private ObjectNode createPropertiesRootNode(JsonNode metaData) {
        ObjectNode properties = null;
        if (hasStandardProperties(metaData)) {
            properties = (ObjectNode) metaData.get("properties");
        } else if (hasAttributesAsProperties(metaData)) {
            properties = (ObjectNode) metaData.get("properties");
        } else if (isBusinessToken(metaData)) {
            if (metaData.get("tokenType").get("properties").isContainerNode()) {
                properties = (ObjectNode) metaData.get("tokenType").get("properties");
            } else {
                properties = objectMapper.createObjectNode();
                if (!metaData.get("tokenType").get("properties").isNull()) {
                    properties.set("properties", metaData.get("tokenType").get("properties"));
                }
            }
        } else {
            properties = objectMapper.createObjectNode();
        }
        return properties;
    }

    private boolean isBusinessToken(JsonNode metaData) {
        return metaData.has("tokenType");
    }

    private boolean hasAttributesAsProperties(JsonNode metaData) {
        return metaData.has("attributes");
    }

    private boolean hasStandardProperties(JsonNode metaData) {
        return metaData.has("properties");
    }
}
