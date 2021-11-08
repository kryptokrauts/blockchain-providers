package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

class MetaDataParserTest {

    private ObjectMapper objectMapper;
    private MetaDataParser parser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        parser = new MetaDataParser(Optional.empty());
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
        assertThat(result.getAttributes().toString()).isEqualTo(
                "[Attribute(type=null, name=Land X, value=82, displayType=number, traitCount=null, maxValue=408), Attribute(type=null, name=Land Y, value=247, "
                + "displayType=number, traitCount=null, maxValue=408)]");
    }

    @Test
    void metadataAsSeperateProperties() throws IOException {
        String metaData = "\n"
                          + "{\"name\":\"Gingerbread Man\",\"description\":\"Gingerbread man is one of the most popular Christmas treat. They are soft in the centers, crisp on "
                          + "the edges, perfectly spiced, molasses and brown sugar-sweetened holiday goodness. Whenever we think of Christmas cookies, gingerbread man come to "
                          + "mind first. Their spice, their molasses flavor, their SMILES, and their charm are obviously irresistible. Gingerbread man, you have our hearts. Need"
                          + " some help when you begin baking? Here is our favorite recipe: https://arkane.network/blog \",\"image\":\"https://raw.githubusercontent"
                          + ".com/ArkaneNetwork/content-management/master/marketing/xmas-2020/Gingerbread.jpg\",\"properties\":{\"Flour\":\"3 cups\",\"Baking powder\":\"3 "
                          + "teaspoons\",\"Gingerbread spices\":\"2 tablespoons\",\"Unsalted butter\":\"6 tablespoons\",\"Eggs\":\"1\",\"Molasses\":\"1 cup\","
                          + "\"tokenTypeId\":3598},\"attributes\":{\"Flour\":\"3 cups\",\"Baking powder\":\"3 teaspoons\",\"Gingerbread spices\":\"2 tablespoons\",\"Unsalted "
                          + "butter\":\"6 tablespoons\",\"Eggs\":\"1\",\"Molasses\":\"1 cup\",\"tokenTypeId\":3598},\"imagePreview\":\"https://raw.githubusercontent"
                          + ".com/ArkaneNetwork/content-management/master/marketing/xmas-2020/Gingerbread.jpg\",\"url\":\"https://arkane.network/blog\","
                          + "\"imageThumbnail\":\"https://raw.githubusercontent.com/ArkaneNetwork/content-management/master/marketing/xmas-2020/Gingerbread.jpg\","
                          + "\"backgroundColor\":\"#d9d9d9\",\"externalUrl\":\"https://arkane.network/blog\",\"tokenTypeId\":3598,\"background_color\":\"#d9d9d9\","
                          + "\"external_url\":\"https://arkane.network/blog\"}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metaData, JsonNode.class));

        assertThat(result.getAttributes()).hasSize(7);
    }

    @Test
    void parsesIpfsImage() throws IOException {
        String metaData = "\n"
                          + "{\"name\":\"Gingerbread Man\",\"description\":\"Gingerbread man is one of the most popular Christmas treat. They are soft in the centers, crisp on "
                          + "the edges, perfectly spiced, molasses and brown sugar-sweetened holiday goodness. Whenever we think of Christmas cookies, gingerbread man come to "
                          + "mind first. Their spice, their molasses flavor, their SMILES, and their charm are obviously irresistible. Gingerbread man, you have our hearts. Need"
                          + " some help when you begin baking? Here is our favorite recipe: https://arkane.network/blog \","
                          + "\"image\":\"ipfs://QmNWtcUL4r5XnWZgmxL3xHYCEFhHYzwMdBXnx2mAdQf8hM\",\"properties\":{\"Flour\":\"3 cups\",\"Baking powder\":\"3 "
                          + "teaspoons\",\"Gingerbread spices\":\"2 tablespoons\",\"Unsalted butter\":\"6 tablespoons\",\"Eggs\":\"1\",\"Molasses\":\"1 cup\","
                          + "\"tokenTypeId\":3598},\"attributes\":{\"Flour\":\"3 cups\",\"Baking powder\":\"3 teaspoons\",\"Gingerbread spices\":\"2 tablespoons\",\"Unsalted "
                          + "butter\":\"6 tablespoons\",\"Eggs\":\"1\",\"Molasses\":\"1 cup\",\"tokenTypeId\":3598},\"imagePreview\":\"https://raw.githubusercontent"
                          + ".com/ArkaneNetwork/content-management/master/marketing/xmas-2020/Gingerbread.jpg\",\"url\":\"https://arkane.network/blog\","
                          + "\"imageThumbnail\":\"https://raw.githubusercontent.com/ArkaneNetwork/content-management/master/marketing/xmas-2020/Gingerbread.jpg\","
                          + "\"backgroundColor\":\"#d9d9d9\",\"externalUrl\":\"https://arkane.network/blog\",\"tokenTypeId\":3598,\"background_color\":\"#d9d9d9\","
                          + "\"external_url\":\"https://arkane.network/blog\"}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metaData, JsonNode.class));

        assertThat(result.getImage().get()).isEqualTo("https://cloudflare-ipfs.com/ipfs/QmNWtcUL4r5XnWZgmxL3xHYCEFhHYzwMdBXnx2mAdQf8hM");
    }

    @Test
    void replacesPinataGateway() throws IOException {
        String metaData = "\n"
                          + "{\"name\":\"Gingerbread Man\",\"description\":\"Gingerbread man is one of the most popular Christmas treat. They are soft in the centers, crisp on "
                          + "the edges, perfectly spiced, molasses and brown sugar-sweetened holiday goodness. Whenever we think of Christmas cookies, gingerbread man come to "
                          + "mind first. Their spice, their molasses flavor, their SMILES, and their charm are obviously irresistible. Gingerbread man, you have our hearts. Need"
                          + " some help when you begin baking? Here is our favorite recipe: https://arkane.network/blog \","
                          + "\"image\":\"https://gateway.pinata.cloud/ipfs/QmdKQ6tJkvq5sa1qkQVCf5RJo74vJQucfZEVPdADoT6VMk\",\"properties\":{\"Flour\":\"3 cups\",\"Baking "
                          + "powder\":\"3 "
                          + "teaspoons\",\"Gingerbread spices\":\"2 tablespoons\",\"Unsalted butter\":\"6 tablespoons\",\"Eggs\":\"1\",\"Molasses\":\"1 cup\","
                          + "\"tokenTypeId\":3598},\"attributes\":{\"Flour\":\"3 cups\",\"Baking powder\":\"3 teaspoons\",\"Gingerbread spices\":\"2 tablespoons\",\"Unsalted "
                          + "butter\":\"6 tablespoons\",\"Eggs\":\"1\",\"Molasses\":\"1 cup\",\"tokenTypeId\":3598},\"imagePreview\":\"https://raw.githubusercontent"
                          + ".com/ArkaneNetwork/content-management/master/marketing/xmas-2020/Gingerbread.jpg\",\"url\":\"https://arkane.network/blog\","
                          + "\"imageThumbnail\":\"https://raw.githubusercontent.com/ArkaneNetwork/content-management/master/marketing/xmas-2020/Gingerbread.jpg\","
                          + "\"backgroundColor\":\"#d9d9d9\",\"externalUrl\":\"https://arkane.network/blog\",\"tokenTypeId\":3598,\"background_color\":\"#d9d9d9\","
                          + "\"external_url\":\"https://arkane.network/blog\"}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metaData, JsonNode.class));

        assertThat(result.getImage().get()).isEqualTo("https://cloudflare-ipfs.com/ipfs/QmdKQ6tJkvq5sa1qkQVCf5RJo74vJQucfZEVPdADoT6VMk");
    }

    @Test
    void parsesIpfsAnimationUrls() throws IOException {
        String metaData = "{\"name\":\"Agumon\",\"description\":\"A Reptile Digimon with an appearance resembling a small dinosaur, it has grown and become able to walk on two "
                          + "legs. Its strength is weak as it is still in the process of growing, but it has a fearless and rather ferocious personality. Hard, sharp claws grow "
                          + "from both its hands and feet, and their power is displayed in battle. It also foreshadows an evolution into a great and powerful Digimon. Its "
                          + "Special Move is spitting a fiery breath from its mouth to attack the opponent (Baby Flame).\",\"image\":\"https://upload.wikimedia"
                          + ".org/wikipedia/commons/thumb/3/3d/Malus-Jonagold.jpg/220px-Malus-Jonagold.jpg\",\"imagePreview\":\"https://upload.wikimedia"
                          + ".org/wikipedia/commons/thumb/3/3d/Malus-Jonagold.jpg/220px-Malus-Jonagold.jpg\",\"imageThumbnail\":\"https://upload.wikimedia"
                          + ".org/wikipedia/commons/thumb/3/3d/Malus-Jonagold.jpg/220px-Malus-Jonagold.jpg\",\"backgroundColor\":\"#FFFFFF\",\"background_color\":\"#FFFFFF\","
                          + "\"animationUrl\":\"ipfs://QmNWtcUL4r5XnWZgmxL3xHYCEFhHYzwMdBXnx2mAdQf8hM\","
                          + "\"animation_url\":\"ipfs://QmNWtcUL4r5XnWZgmxL3xHYCEFhHYzwMdBXnx2mAdQf8hM\",\"externalUrl\":\"https://wikimon.net/Agumon\","
                          + "\"external_url\":\"https://wikimon.net/Agumon\",\"animationUrls\":[{\"type\":\"video\","
                          + "\"value\":\"ipfs://QmNWtcUL4r5XnWZgmxL3xHYCEFhHYzwMdBXnx2mAdQf8hM\"}],\"attributes\":[{\"type\":\"property\",\"name\":\"Level\",\"value\":\"Child\","
                          + "\"traitType\":\"Level\",\"trait_type\":\"Level\"},{\"type\":\"property\",\"name\":\"Type\",\"value\":\"Reptile\",\"traitType\":\"Type\","
                          + "\"trait_type\":\"Type\"},{\"type\":\"property\",\"name\":\"Attribute\",\"value\":\"Vaccine\",\"traitType\":\"Attribute\","
                          + "\"trait_type\":\"Attribute\"},{\"type\":\"stat\",\"name\":\"Min. weight\",\"value\":\"15\",\"maxValue\":\"20\",\"displayType\":\"number\","
                          + "\"display_type\":\"number\",\"traitType\":\"Min. weight\",\"trait_type\":\"Min. weight\"}],"
                          + "\"contract\":{\"address\":\"0x42a910ee009edbcb6e821522971e5fc13b51ecb9\",\"name\":\"Digimon\",\"symbol\":\"DIGIMON\",\"image\":\"https://static"
                          + ".wikia.nocookie.net/digimon/images/b/b2/Digimon_Adventure.jpg/revision/latest/scale-to-width-down/340?cb=20100619144849\","
                          + "\"imageUrl\":\"https://static.wikia.nocookie.net/digimon/images/b/b2/Digimon_Adventure"
                          + ".jpg/revision/latest/scale-to-width-down/340?cb=20100619144849\",\"image_url\":\"https://static.wikia.nocookie"
                          + ".net/digimon/images/b/b2/Digimon_Adventure.jpg/revision/latest/scale-to-width-down/340?cb=20100619144849\",\"description\":\"Digimon (デジモン Dejimon?)"
                          + ", short for (デジタルモンスター Dejitaru Monsutā?, \\\"Digital Monster\\\") is a popular Japanese series of media and merchandise created by Akiyoshi Hongo, "
                          + "which is comprised of anime, manga, toys, video games, trading card games and other media. Digimon, the lifeforms the series revolves around, are "
                          + "monsters of various forms living in a \\\"Digital World,\\\" a parallel universe that originated from Earth's various communication networks.\","
                          + "\"externalLink\":\"https://en.wikipedia.org/wiki/Digimon\",\"external_link\":\"https://en.wikipedia.org/wiki/Digimon\",\"externalUrl\":\"https://en"
                          + ".wikipedia.org/wiki/Digimon\",\"external_url\":\"https://en.wikipedia.org/wiki/Digimon\",\"media\":[{\"type\":\"youtube\",\"value\":\"https://www"
                          + ".youtube.com/embed/2nHDi5sDJhk\"}],\"type\":\"ERC_1155\"},\"asset_contract\":{\"address\":\"0x42a910ee009edbcb6e821522971e5fc13b51ecb9\","
                          + "\"name\":\"Digimon\",\"symbol\":\"DIGIMON\",\"image\":\"https://static.wikia.nocookie.net/digimon/images/b/b2/Digimon_Adventure"
                          + ".jpg/revision/latest/scale-to-width-down/340?cb=20100619144849\",\"imageUrl\":\"https://static.wikia.nocookie"
                          + ".net/digimon/images/b/b2/Digimon_Adventure.jpg/revision/latest/scale-to-width-down/340?cb=20100619144849\",\"image_url\":\"https://static.wikia"
                          + ".nocookie.net/digimon/images/b/b2/Digimon_Adventure.jpg/revision/latest/scale-to-width-down/340?cb=20100619144849\",\"description\":\"Digimon (デジモン "
                          + "Dejimon?), short for (デジタルモンスター Dejitaru Monsutā?, \\\"Digital Monster\\\") is a popular Japanese series of media and merchandise created by "
                          + "Akiyoshi Hongo, which is comprised of anime, manga, toys, video games, trading card games and other media. Digimon, the lifeforms the series "
                          + "revolves around, are monsters of various forms living in a \\\"Digital World,\\\" a parallel universe that originated from Earth's various "
                          + "communication networks.\",\"externalLink\":\"https://en.wikipedia.org/wiki/Digimon\",\"external_link\":\"https://en.wikipedia.org/wiki/Digimon\","
                          + "\"externalUrl\":\"https://en.wikipedia.org/wiki/Digimon\",\"external_url\":\"https://en.wikipedia.org/wiki/Digimon\","
                          + "\"media\":[{\"type\":\"youtube\",\"value\":\"https://www.youtube.com/embed/2nHDi5sDJhk\"}],\"type\":\"ERC_1155\"},\"fungible\":false}";

        NonFungibleMetaData result = parser.parseMetaData(objectMapper.readValue(metaData, JsonNode.class));

        assertThat(result.getAnimationUrls().get(0).getValue()).isEqualTo("https://cloudflare-ipfs.com/ipfs/QmNWtcUL4r5XnWZgmxL3xHYCEFhHYzwMdBXnx2mAdQf8hM");
        assertThat(result.getAnimationUrl().get()).isEqualTo("https://cloudflare-ipfs.com/ipfs/QmNWtcUL4r5XnWZgmxL3xHYCEFhHYzwMdBXnx2mAdQf8hM");
    }
}
