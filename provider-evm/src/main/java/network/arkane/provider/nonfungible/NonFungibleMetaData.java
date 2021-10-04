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
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.ipfs.IpfsUtil;
import network.arkane.provider.nonfungible.animationtype.AnimationTypeComparator;
import network.arkane.provider.nonfungible.animationtype.AnimationUrlParser;
import network.arkane.provider.nonfungible.domain.Attribute;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.nonfungible.domain.TypeValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Data
@Slf4j
public class NonFungibleMetaData {
    private static final AnimationTypeComparator ANIMATION_TYPE_COMPARATOR = new AnimationTypeComparator();
    private static final String TYPE_UNKNOWN = "unknown";

    private JsonNode json;
    private ObjectMapper objectMapper;
    private AnimationUrlParser animationUrlParser;

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
                             getProperty("image_url"))
                     .filter(StringUtils::isNotBlank)
                     .map(this::replaceIpfsLink)
                     .findFirst();
    }

    private String replaceIpfsLink(String imgUrl) {
        return IpfsUtil.replaceIpfsLink(imgUrl);
    }

    private TypeValue replaceIpfsLink(TypeValue typeValue) {
        if (typeValue != null && StringUtils.isNotBlank(typeValue.getValue()) && typeValue.getValue().startsWith("ipfs")) {
            return TypeValue.builder()
                            .type(typeValue.getType())
                            .value(replaceIpfsLink(typeValue.getValue()))
                            .build();
        }
        return typeValue;
    }

    public Optional<String> getBackgroundColor() {
        return Stream.of(
                             getProperty("backgroundColor"),
                             getProperty("background_color")
                        ).filter(StringUtils::isNotBlank)
                     .findFirst();
    }

    public Optional<String> getAnimationUrl() {
        List<TypeValue> animationUrls = getAnimationUrls();
        return CollectionUtils.isEmpty(animationUrls)
               ? Optional.empty()
               : Optional.of(animationUrls.stream()
                                          .min(ANIMATION_TYPE_COMPARATOR)
                                          .map(TypeValue::getValue)
                                          .map(this::replaceIpfsLink)
                                          .orElse(String.valueOf(animationUrls.get(0).getValue())));
    }

    public List<TypeValue> getAnimationUrls() {
        if (json.has("animationUrls")) {
            try {
                final List<TypeValue> animationUrls = objectMapper.readValue(getProperty("animationUrls"), new TypeReference<List<TypeValue>>() {});
                return animationUrls.stream()
                                    .map(tv -> tv.getType().equalsIgnoreCase(TYPE_UNKNOWN)
                                               ? animationUrlParser.parse(tv.getValue())
                                               : tv)
                                    .map(this::replaceIpfsLink)
                                    .collect(toList());
            } catch (IOException e) {
                log.debug("Error when parsing animationUrls", e);
            }
        } else {
            return Stream.of(getProperty("animationUrl"),
                             getProperty("animation_url"))
                         .filter(StringUtils::isNotBlank)
                         .findFirst()
                         .map(aUrl -> singletonList(replaceIpfsLink(animationUrlParser.parse(aUrl))))
                         .orElseGet(Collections::emptyList);
        }
        return emptyList();
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

    public List<Attribute> getAttributes() {
        return Stream.of(
                             parseAttributes("attributes"),
                             parseAttributes("properties")
                        )
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findFirst()
                     .orElse(emptyList());
    }


    public Optional<NonFungibleContract> getContract() {
        return Stream.of(
                             parseContract("contract"),
                             parseContract("asset_contract"))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findFirst();

    }

    public Optional<List<Attribute>> parseAttributes(String propertyName) {
        if (!json.has(propertyName)) return Optional.empty();

        String propValue = getProperty(propertyName);

        try {
            if (json.get(propertyName).isArray()) {
                List<Attribute> attributes = objectMapper.readValue(propValue, new TypeReference<List<Attribute>>() {});
                return Optional.of(attributes);
            } else {
                List<Attribute> attributes = new ArrayList<>();
                json.get(propertyName).fields().forEachRemaining(entry -> {
                    attributes.add(Attribute.builder().type("property").name(entry.getKey()).value(entry.getValue().toString()).build());
                });
                return Optional.of(attributes);
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
