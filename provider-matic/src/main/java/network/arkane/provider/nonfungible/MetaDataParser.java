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
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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


    @Cacheable(value = "non-fungibles-meta-data", key = "{#contractAddress, #tokenId}")
    @SneakyThrows
    public NonFungibleMetaData parseMetaData(String tokenId,
                                             String contractType,
                                             String contractAddress) {
        try {
            ContractCall metadataCall = "ERC-721".equalsIgnoreCase(contractType)
                                        ? createErc721UriCall(contractAddress, tokenId)
                                        : createErc1155UriCall(contractAddress, tokenId);

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
        NonFungibleMetaData.NonFungibleMetaDataBuilder metaDataBuilder = NonFungibleMetaData.builder();

        ObjectNode tokenMetaData = createPropertiesRootNode(metaData);

        if (metaData.hasNonNull("imageUrl")) {
            tokenMetaData.put("image", metaData.get("imageUrl").asText());
        }
        if (metaData.hasNonNull("image")) {
            tokenMetaData.put("image", metaData.get("image").asText());
        }
        if (metaData.hasNonNull("description")) {
            tokenMetaData.put("description", metaData.get("description").asText());
        }
        if (metaData.hasNonNull("name")) {
            tokenMetaData.put("name", metaData.get("name").asText());
        }
        if (metaData.hasNonNull("tokenTypeId")) {
            tokenMetaData.put("tokenTypeId", metaData.get("tokenTypeId").asText());
        }
        parseBackground(metaData, tokenMetaData);
        parseAnimation(metaData, tokenMetaData);
        parseExternalUrl(metaData, tokenMetaData);

        addTokenTypeFieldsToProperties(metaData, tokenMetaData);

        metaDataBuilder.properties(tokenMetaData);

        return metaDataBuilder.build();
    }

    private Optional<String> parseProperty(JsonNode metaData,
                                           String... possibleProperties) {
        return Arrays.stream(possibleProperties)
                     .filter(metaData::hasNonNull)
                     .map(prop -> metaData.get(prop).asText())
                     .findFirst();
    }

    private void parseExternalUrl(JsonNode metaData,
                                  ObjectNode tokenMetaData) {
        parseProperty(metaData, "externalUrl", "external_url", "url")
                .ifPresent(val -> {
                    tokenMetaData.put("externalUrl", val);
                    tokenMetaData.put("external_url", val);
                    tokenMetaData.put("url", val);
                });
    }

    private void parseAnimation(JsonNode metaData,
                                ObjectNode tokenMetaData) {
        parseProperty(metaData, "animationUrl", "animation_url")
                .ifPresent(val -> {
                    tokenMetaData.put("animationUrl", val);
                    tokenMetaData.put("animation_url", val);
                });
    }

    private void parseBackground(JsonNode metaData,
                                 ObjectNode tokenMetaData) {
        parseProperty(metaData, "backgroundColor", "background_color")
                .ifPresent(val -> {
                    tokenMetaData.put("backgroundColor", val);
                    tokenMetaData.put("background_color", val);
                });
    }

    private void addTokenTypeFieldsToProperties(JsonNode metaData,
                                                ObjectNode properties) {
        if (metaData.has("tokenType")) {
            JsonNode tokenType = metaData.get("tokenType");
            properties.put("tokenTypeId", tokenType.get("id").asText(""));
            properties.put("name", tokenType.get("name").asText(""));
            properties.put("description", tokenType.get("description").asText(""));
            properties.put("backgroundColor", tokenType.get("backgroundColor").asText(""));
            properties.put("background_color", tokenType.get("backgroundColor").asText(""));
            properties.put("animationUrl", tokenType.get("animationUrl").asText(""));
            properties.put("animation_url", tokenType.get("animationUrl").asText(""));
            properties.put("externalUrl", tokenType.get("externalUrl").asText(""));
            properties.put("external_url", tokenType.get("externalUrl").asText(""));
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
            properties = (ObjectNode) metaData.get("attributes");
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
