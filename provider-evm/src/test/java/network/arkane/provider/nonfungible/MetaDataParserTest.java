package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.contract.EvmContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MetaDataParserTest {

    private ObjectMapper objectMapper;
    private MetaDataParser parser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        parser = new MetaDataParser(mock(EvmContractService.class), Optional.empty());
    }

    @Test
    void parsesBusinessItems() throws IOException {
        String metadata = "{\n"
                          + "            \"name\": \"Silkie\",\n"
                          + "            \"description\": \"The Silkie (sometimes spelled Silky) is a breed of chicken named for its atypically fluffy plumage, which is said to "
                          + "feel like silk and satin. The breed has several other unusual qualities, such as black skin and bones, blue earlobes, and five toes on each foot, "
                          + "whereas most chickens only have four. They are often exhibited in poultry shows, and appear in various colors. In addition to their distinctive "
                          + "physical characteristics, Silkies are well known for their calm, friendly temperament. It is among the most docile of poultry. Hens are also "
                          + "exceptionally broody, and care for young well. Though they are fair layers themselves, laying only about three eggs a week, they are commonly used "
                          + "to hatch eggs from other breeds and bird species due to their broody nature. Silkie chickens are very easy to keep as pets. They are suitable for "
                          + "children, but like any pet, should be handled with care.\",\n"
                          + "            \"image\": \"https://upload.wikimedia.org/wikipedia/commons/3/36/A_fuzzy_baby_chicken_and_its_mom.jpg\",\n"
                          + "            \"properties\": {\n"
                          + "                \"trait_type\": \"Cuteness\",\n"
                          + "                \"value\": \"SuperrrrrCute\",\n"
                          + "                \"tokenTypeId\": 140\n"
                          + "            },\n"
                          + "            \"attributes\": {\n"
                          + "                \"trait_type\": \"Cuteness\",\n"
                          + "                \"value\": \"SuperrrrrCute\",\n"
                          + "                \"tokenTypeId\": 140\n"
                          + "            },\n"
                          + "            \"imagePreview\": \"https://upload.wikimedia.org/wikipedia/commons/3/36/A_fuzzy_baby_chicken_and_its_mom.jpg\",\n"
                          + "            \"url\": \"https://metadata-qa.arkane.network/api/apps/05a51091-54b8-43aa-9c12-142e2b3d9c04/contracts/62/token-types/140/metadata\",\n"
                          + "            \"imageThumbnail\": \"https://upload.wikimedia.org/wikipedia/commons/3/36/A_fuzzy_baby_chicken_and_its_mom.jpg\",\n"
                          + "            \"backgroundColor\": \"#111111\",\n"
                          + "            \"externalUrl\": \"https://metadata-qa.arkane"
                          + ".network/api/apps/05a51091-54b8-43aa-9c12-142e2b3d9c04/contracts/62/token-types/140/metadata\",\n"
                          + "            \"tokenTypeId\": 140,\n"
                          + "            \"background_color\": \"#111111\",\n"
                          + "            \"external_url\": \"https://metadata-qa.arkane"
                          + ".network/api/apps/05a51091-54b8-43aa-9c12-142e2b3d9c04/contracts/62/token-types/140/metadata\"\n"
                          + "        }\n"
                          + "    }";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metadata, JsonNode.class));

        assertThat(result).isNotNull();

        assertThat(result.getName()).isEqualTo("Silkie");
        assertThat(result.getDescription()).isEqualTo(
                "The Silkie (sometimes spelled Silky) is a breed of chicken named for its atypically fluffy plumage, which is said to feel like silk and satin. The breed has "
                + "several other unusual qualities, such as black skin and bones, blue earlobes, and five toes on each foot, whereas most chickens only have four. They are often"
                + " exhibited in poultry shows, and appear in various colors. In addition to their distinctive physical characteristics, Silkies are well known for their calm, "
                + "friendly temperament. It is among the most docile of poultry. Hens are also exceptionally broody, and care for young well. Though they are fair layers "
                + "themselves, laying only about three eggs a week, they are commonly used to hatch eggs from other breeds and bird species due to their broody nature. Silkie "
                + "chickens are very easy to keep as pets. They are suitable for children, but like any pet, should be handled with care.");
        assertThat(result.getImage().get()).isEqualTo(
                "https://upload.wikimedia.org/wikipedia/commons/3/36/A_fuzzy_baby_chicken_and_its_mom.jpg");
        assertThat(result.getAttributes().toString()).isEqualTo("[Trait(traitType=Cuteness, value=SuperrrrrCute, displayType=null, traitCount=null, maxValue=null)]");
    }

    @Test
    void parseSerra() throws IOException {
        String metaData = "\n"
                          + " {\n"
                          + " \t\"name\": \"Base Set Crate\",\n"
                          + " \t\"description\": \"Contains the 90 free Base Set Cards in higher Qualities.\",\n"
                          + " \t\"image\": \"http://metadata.synergyofserra.com/images/340282366920938463463374607431768211456.png\",\n"
                          + " \t\"external_url\": \"https://synergyofserra.com/shop\",\n"
                          + " \t\"background_color\": \"000000\",\n"
                          + " \t\"animation_url\": \"http://metadata.synergyofserra.com/videos/340282366920938463463374607431768211456.webm\"\n"
                          + " }";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metaData, JsonNode.class));


        assertThat(result).isNotNull();

        assertThat(result.getName()).isEqualTo("Base Set Crate");
        assertThat(result.getDescription()).isEqualTo(
                "Contains the 90 free Base Set Cards in higher Qualities.");
        assertThat(result.getImage().get()).isEqualTo(
                "http://metadata.synergyofserra.com/images/340282366920938463463374607431768211456.png");
        assertThat(result.getAnimationUrl().get()).isEqualTo(
                "http://metadata.synergyofserra.com/videos/340282366920938463463374607431768211456.webm");
        assertThat(result.getExternalUrl().get()).isEqualTo(
                "https://synergyofserra.com/shop");
    }

    @Test
    void parseLand() throws IOException {
        String metaData = "\n"
                          + "{\"name\":\"LAND (-122, 43)\",\"description\":\"A LAND is a digital piece of real estate in The Sandbox metaverse that players can buy to build "
                          + "experiences on top of. Once you own a LAND, you will be able to populate it with Games and Assets. Each LAND is a unique (non-fungible) token lying "
                          + "on the public Ethereum blockchain (ERC-721).\",\"image\":\"https://www.sandbox.game/img/18_Land/land.png\",\"properties\":[{\"trait_type\":\"Land "
                          + "X\",\"value\":82,\"max_value\":408,\"display_type\":\"number\"},{\"trait_type\":\"Land Y\",\"value\":247,\"max_value\":408,"
                          + "\"display_type\":\"number\"}],\"external_url\":\"https://www.sandbox.game/en/lands/0ef9f207-50d4-40c1-ad47-33046138f741/\"}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metaData, JsonNode.class));

        assertThat(result.getName()).isEqualTo("LAND (-122, 43)");
        assertThat(result.getDescription()).isEqualTo(
                "A LAND is a digital piece of real estate in The Sandbox metaverse that players can buy to build experiences on top of. Once you own a LAND, you will be able to "
                + "populate it with Games and Assets. Each LAND is a unique (non-fungible) token lying on the public Ethereum blockchain (ERC-721).");
        assertThat(result.getExternalUrl().get()).isEqualTo("https://www.sandbox.game/en/lands/0ef9f207-50d4-40c1-ad47-33046138f741/");
        assertThat(result.getImage().get()).isEqualTo("https://www.sandbox.game/img/18_Land/land.png");
        assertThat(result.getAttributes().toString()).isEqualTo("[Trait(traitType=Land X, value=82, displayType=number, traitCount=null, maxValue=408), "
                                                                + "Trait(traitType=Land Y, value=247, displayType=number, traitCount=null, maxValue=408)]");
    }
}
