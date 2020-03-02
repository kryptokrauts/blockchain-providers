package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Data
public class NonFungibleMetaData {
    private String title;
    private String type;
    private JsonNode properties;

    public String getProperty(String propertyName) {
        return properties.has(propertyName)
               ? (properties.get(propertyName).isContainerNode() ? properties.get(propertyName).toString() : properties.get(propertyName).asText())
               : null;
    }

    public String getName() {
        return getProperty("name");
    }

    public String getDescription() {
        return getProperty("description");
    }

    public String getImage() {
        return getProperty("image");
    }
}
