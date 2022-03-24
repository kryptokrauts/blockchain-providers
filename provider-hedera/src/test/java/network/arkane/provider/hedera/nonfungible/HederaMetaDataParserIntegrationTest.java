package network.arkane.provider.hedera.nonfungible;

import network.arkane.provider.nonfungible.NonFungibleMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HederaMetaDataParserIntegrationTest {

    private HederaMetaDataParser parser;

    @BeforeEach
    void setUp() {
        parser = new HederaMetaDataParser(Optional.empty());
    }

    @Test
    void parsesHashAxisNft() {
        NonFungibleMetaData metaData = parser.parseMetaData("0.0.639348", 1L, "YmFma3JlaWVvcWt1ZmpndnRsbjdqcHltaTNncnRtN25hZnN0NjJ0c2FyajMyaDRqYXp4dGFobXV5NjQ=");

        assertThat(metaData).isNotNull();
    }
}
