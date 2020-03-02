package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.contract.MaticContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MetaDataParserTest {

    private ObjectMapper objectMapper;
    private MetaDataParser parser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        parser = new MetaDataParser(objectMapper, mock(MaticContractService.class));
    }

    @Test
    void parsesErc721() throws IOException {
        String metadata = "{"
                          + "    \"title\": \"Asset Metadata\","
                          + "    \"type\": \"object\","
                          + "    \"properties\": {"
                          + "        \"name\": \"cryptopups\","
                          + "        \"description\": \"cryptopuppies\","
                          + "        \"image\": \"https://via.placeholder.com/150" + "\""
                          + "    }"
                          + "}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metadata, JsonNode.class));

        assertThat(result).isNotNull();

        assertThat(result.getProperties().toString()).isEqualToIgnoringCase("{\"name\":\"cryptopups\",\"description\":\"cryptopuppies\",\"image\":\"https://via.placeholder"
                                                                            + ".com/150\"}");

        assertThat(result.getTitle()).isEqualTo("Asset Metadata");
        assertThat(result.getType()).isEqualTo("object");
        assertThat(result.getName()).isEqualTo("cryptopups");
        assertThat(result.getDescription()).isEqualTo("cryptopuppies");
        assertThat(result.getImage()).isEqualTo("https://via.placeholder.com/150");
    }

    @Test
    void parsesErc1155() throws IOException {
        String metadata = "{\n"
                          + "    \"title\": \"Asset Metadata\",\n"
                          + "    \"type\": \"object\",\n"
                          + "    \"properties\": {\n"
                          + "        \"name\": \"cryptopups\",\n"
                          + "        \"description\": \"cryptopuppies\",\n"
                          + "        \"image\": \"https://via.placeholder.com/150\",\n"
                          + "        \"properties\": {\n"
                          + "            \"arrayProperty\" : [\"a\", \"b\", \"c\"],\n"
                          + "            \"anotherProperty\": \"aa\",\n"
                          + "            \"yaProperty\": 3\n"
                          + "        }\n"
                          + "    }\n"
                          + "}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metadata, JsonNode.class));

        assertThat(result).isNotNull();

        assertThat(result.getProperties().toString()).isEqualToIgnoringCase("{\"name\":\"cryptopups\",\"description\":\"cryptopuppies\",\"image\":\"https://via.placeholder"
                                                                            + ".com/150\",\"properties\":{\"arrayProperty\":[\"a\",\"b\",\"c\"],\"anotherProperty\":\"aa\","
                                                                            + "\"yaProperty\":3}}");

        assertThat(result.getTitle()).isEqualTo("Asset Metadata");
        assertThat(result.getType()).isEqualTo("object");
        assertThat(result.getName()).isEqualTo("cryptopups");
        assertThat(result.getDescription()).isEqualTo("cryptopuppies");
        assertThat(result.getImage()).isEqualTo("https://via.placeholder.com/150");
    }
}
