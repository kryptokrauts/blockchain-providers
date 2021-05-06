package network.arkane.provider.nonfungible.animationtype;

import network.arkane.provider.nonfungible.domain.TypeValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnimationUrlParserTest {

    private AnimationTypeByExtensionMapper byExtensionMapper;

    private AnimationUrlParser parser;

    @BeforeEach
    void setUp() {
        byExtensionMapper = mock(AnimationTypeByExtensionMapper.class);

        parser = new AnimationUrlParser(byExtensionMapper);
    }

    @Test
    void parse() {
        final String url = "http://07d0df3d-fa8e-4ccb-bcde-e27a65f88377.gif";
        final String type = "image";

        when(byExtensionMapper.map(url)).thenReturn(Optional.of(type));

        final TypeValue typeValue = parser.parse(url);

        assertThat(typeValue).isNotNull();
        assertThat(typeValue.getType()).isEqualTo(type);
        assertThat(typeValue.getValue()).isEqualTo(url);
    }

    @Test
    void parseUnknownType() {
        final String url = "http://f7158339-e1c0-4abf-ac8d-6c57e2fc6395.foo";

        when(byExtensionMapper.map(url)).thenReturn(Optional.empty());

        final TypeValue typeValue = parser.parse(url);

        assertThat(typeValue).isNotNull();
        assertThat(typeValue.getType()).isEqualTo("unknown");
        assertThat(typeValue.getValue()).isEqualTo(url);
    }
}
