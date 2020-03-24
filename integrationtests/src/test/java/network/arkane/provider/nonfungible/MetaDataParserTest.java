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
        parser = new MetaDataParser(mock(MaticContractService.class));
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
        String metadata = "{"
                          + "    \"title\": \"Asset Metadata\","
                          + "    \"type\": \"object\","
                          + "    \"properties\": {"
                          + "        \"name\": \"cryptopups\","
                          + "        \"description\": \"cryptopuppies\","
                          + "        \"image\": \"https://via.placeholder.com/150\","
                          + "        \"properties\": {"
                          + "            \"arrayProperty\" : [\"a\", \"b\", \"c\"],"
                          + "            \"anotherProperty\": \"aa\","
                          + "            \"yaProperty\": 3"
                          + "        }"
                          + "    }"
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

    @Test
    void parsesBusinessItems() throws IOException {
        String metadata = ""
                          + "{\"secretType\":\"VECHAIN\",\"contractAddress\":\"0xf9f9435af4bf735b8a02d01ca558a532505902fa\","
                          + "\"contractTokenId\":57896044618658097711785492504343953930378098368950605117825912685706015145988,\"tokenType\":{\"id\":29,\"name\":\"Lockheed "
                          + "AC-130\",\"description\":\"The Lockheed AC-130 gunship is a heavily armed, long-endurance, ground-attack variant of the C-130 Hercules transport, "
                          + "fixed-wing aircraft. It carries a wide array of ground attack weapons that are integrated with sophisticated sensors, navigation, and fire-control "
                          + "systems. Unlike other modern military fixed-wing aircraft, the AC-130 relies on visual targeting. Because its large profile and low operating "
                          + "altitudes of approximately 7,000 feet (2,100 m) make it an easy target, its close air support missions are usually flown at night.\",\"nf\":true,"
                          + "\"decimals\":0,\"contractTypeId\":57896044618658097711785492504343953930378098368950605117825912685706015145984,"
                          + "\"transactionHash\":\"0x5d7ecdbae848a66f76ccffb248ea563008573be046ed722f70e68bab95639851\",\"confirmed\":true,\"mineDate\":\"2020-01-27T15:15:00"
                          + ".000+0000\",\"transactionDate\":\"2020-01-27T15:14:49.377+0000\",\"tokenContract\":{\"id\":9,"
                          + "\"address\":\"0xf9f9435af4bf735b8a02d01ca558a532505902fa\",\"confirmed\":true,\"deployDate\":\"2019-11-12T08:55:46.090+0000\","
                          + "\"mineDate\":\"2019-11-12T08:55:50.000+0000\",\"description\":\"The Warplanes Game\",\"applicationId\":\"1f64ded9-2a05-4824-b682-661023359357\","
                          + "\"name\":\"Warplanes\",\"transactionHash\":\"0xb73fcd3062f24a0c3fb3d33314ed44afff1f923daa51724cc03676251d3fe405\","
                          + "\"owner\":\"31d391a2-1a9a-4f87-9ab7-16cbb80563b6\",\"secretType\":\"VECHAIN\"},\"properties\":\"\",\"backgroundColor\":\"#c8e2eb\","
                          + "\"image\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29"
                          + ".jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg\",\"imageThumbnail\":\"https://upload.wikimedia"
                          + ".org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29.jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg\","
                          + "\"imagePreview\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29"
                          + ".jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg\"},\"amount\":1,\"imageUrl\":\"https://upload.wikimedia"
                          + ".org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29.jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg\","
                          + "\"imagePreviewUrl\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29"
                          + ".jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg\",\"imageThumbnailUrl\":\"https://upload.wikimedia"
                          + ".org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29.jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg\","
                          + "\"backgroundColor\":\"#c8e2eb\"}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metadata, JsonNode.class));

        assertThat(result).isNotNull();

        assertThat(result.getProperties().toString()).isEqualToIgnoringCase("{\"properties\":\"\",\"image\":\"https://upload.wikimedia"
                                                                            + ".org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29"
                                                                            + ".jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg\",\"name\":\"Lockheed AC-130\","
                                                                            + "\"description\":\"The Lockheed AC-130 gunship is a heavily armed, long-endurance, ground-attack "
                                                                            + "variant of the C-130 Hercules transport, fixed-wing aircraft. It carries a wide array of ground "
                                                                            + "attack weapons that are integrated with sophisticated sensors, navigation, and fire-control "
                                                                            + "systems. Unlike other modern military fixed-wing aircraft, the AC-130 relies on visual targeting. "
                                                                            + "Because its large profile and low operating altitudes of approximately 7,000 feet (2,100 m) make "
                                                                            + "it an easy target, its close air support missions are usually flown at night.\","
                                                                            + "\"backgroundColor\":\"#c8e2eb\"}");

        assertThat(result.getTitle()).isNull();
        assertThat(result.getType()).isNull();
        assertThat(result.getName()).isEqualTo("Lockheed AC-130");
        assertThat(result.getDescription()).isEqualTo(
                "The Lockheed AC-130 gunship is a heavily armed, long-endurance, ground-attack variant of the C-130 Hercules transport, fixed-wing aircraft. It carries a wide "
                + "array of ground attack weapons that are integrated with sophisticated sensors, navigation, and fire-control systems. Unlike other modern military fixed-wing "
                + "aircraft, the AC-130 relies on visual targeting. Because its large profile and low operating altitudes of approximately 7,000 feet (2,100 m) make it an easy "
                + "target, its close air support missions are usually flown at night.");
        assertThat(result.getImage()).isEqualTo(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/AC-130_Spectre_Spooky_%282152191923%29.jpg/600px-AC-130_Spectre_Spooky_%282152191923%29.jpg");
    }
}
