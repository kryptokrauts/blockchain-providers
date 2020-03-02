package network.arkane.provider.nonfungible;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;

class NonFungibleMetaDataTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void toDto() throws IOException {
        NonFungibleMetaData metaData = objectMapper.readValue("{\n"
                                                              + "  \"name\": \"Herbie Starbelly\",\n"
                                                              + "  \"description\": \"Friendly OpenSea Creature that enjoys long swims in the ocean.\",\n"
                                                              + "  \"image\": \"https://storage.googleapis.com/opensea-prod.appspot.com/creature/50.png\",\n"
                                                              + "  \"attributes\": [...]\n"
                                                              + "}", NonFungibleMetaData.class);

        assertThat(metaData).isNotNull();
    }
}
