package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.nonfungible.domain.Trait;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Data
public class NonFungibleMetaData {
    private JsonNode json;
    private ObjectMapper objectMapper;

    public String getProperty(String propertyName) {
        return json.has(propertyName)
               ? (json.get(propertyName).isContainerNode() ? json.get(propertyName).toString() : json.get(propertyName).asText())
               : null;
    }

    public void setProperty(String propertyName,
                            String value) {
        ((ObjectNode) json).put(propertyName, value);
    }

    public String getName() {
        return getProperty("name");
    }

    public String getDescription() {
        return getProperty("description");
    }

    public Optional<String> getImage() {
        return Stream.of(
                getProperty("image"),
                getProperty("image_data"),
                getProperty("imageUrl"),
                getProperty("image_url")
                        ).filter(StringUtils::isNotBlank)
                     .findFirst();
    }

    public Optional<String> getBackgroundColor() {
        return Stream.of(
                getProperty("backgroundColor"),
                getProperty("background_color")
                        ).filter(StringUtils::isNotBlank)
                     .findFirst();
    }

    public Optional<String> getAnimationUrl() {
        return Stream.of(
                getProperty("animationUrl"),
                getProperty("animation_url")
                        ).filter(StringUtils::isNotBlank)
                     .findFirst();
    }

    public Optional<String> getExternalUrl() {
        return Stream.of(
                getProperty("externalUrl"),
                getProperty("external_url"),
                getProperty("url")
                        ).filter(StringUtils::isNotBlank)
                     .findFirst();
    }

    public Boolean getFungible() {
        return Stream.of(getProperty("fungible"))
                     .filter(StringUtils::isNotBlank)
                     .map(Boolean::parseBoolean)
                     .findFirst()
                     .orElse(false);
    }

    public List<Trait> getAttributes() {
        return Stream.of(
                parseAttributes("attributes"),
                parseAttributes("properties")
                        )
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findFirst()
                     .orElse(Collections.emptyList());
    }

    public Optional<NonFungibleContract> getContract() {
        return Stream.of(
                parseContract("contract"),
                parseContract("asset_contract"))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findFirst();

    }

    public Optional<List<Trait>> parseAttributes(String propertyName) {
        if (!json.has(propertyName)) return Optional.empty();

        String propValue = getProperty(propertyName);

        try {
            if (json.get(propertyName).isArray()) {
                List<Trait> traits = objectMapper.readValue(propValue, new TypeReference<List<Trait>>() {});
                return Optional.of(traits);
            } else {
                List<Trait> traits = new ArrayList<>();
                json.get(propertyName).fields().forEachRemaining(entry -> {
                    traits.add(Trait.builder().traitType(entry.getKey()).value(entry.getValue().toString()).build());
                });
                return Optional.of(traits);
            }

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<NonFungibleContract> parseContract(String propertyName) {
        if (!json.has(propertyName)) return Optional.empty();
        String propValue = getProperty(propertyName);
        try {
            return Optional.of(objectMapper.readValue(propValue, NonFungibleContract.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void setDescription(String description) {
        setProperty("description", description);
    }

    public void setImage(String image) {
        setProperty("image", image);
    }
}
