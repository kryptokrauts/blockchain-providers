package network.arkane.provider.nonfungible.animationtype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AnimationTypeByExtensionMapperTest {

    private static final String IMAGE_TYPE = "image";
    private static final String VIDEO_TYPE = "video";
    private static final String AUDIO_TYPE = "audio";
    private static final String THREE_D_TYPE = "3d";
    private static final List<String> URL_TEMPLATES = Arrays.asList("https://png.pngtree.com/element_our/20200610/ourmid/pngtree-wrong-number-image_2248568.%s",
                                                                    "https://png.pngtree.com/element_our/20200610/ourmid/pngtree-wrong-number-image_2248568.%s?sfhgdjgfhg=tryjuy");
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("gif", "png", "jpg", "jpeg", "bmp", "webp", "svg");
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("mp4", "webm", "m4v", "ogv", "ogm", "ogg");
    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList("mp3", "mav", "oga");
    private static final List<String> THREE_D_EXTENSIONS = Arrays.asList("glb", "gltf");

    private AnimationTypeByExtensionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AnimationTypeByExtensionMapper();
    }

    @ParameterizedTest
    @MethodSource("args")
    void map(final String extension,
             final String expectedType,
             final String urlTemplate) {
        final String url = String.format(urlTemplate, extension);

        final Optional<String> result = mapper.map(url);

        assertThat(result).contains(expectedType);
    }

    private static Stream<Arguments> args() {
        return URL_TEMPLATES.stream()
                            .flatMap(urlTemplate -> Stream.of(IMAGE_EXTENSIONS.stream()
                                                                              .map(extension -> Arguments.of(extension, IMAGE_TYPE, urlTemplate)),
                                                              VIDEO_EXTENSIONS.stream()
                                                                              .map(extension -> Arguments.of(extension, VIDEO_TYPE, urlTemplate)),
                                                              AUDIO_EXTENSIONS.stream()
                                                                              .map(extension -> Arguments.of(extension, AUDIO_TYPE, urlTemplate)),
                                                              THREE_D_EXTENSIONS.stream()
                                                                                .map(extension -> Arguments.of(extension, THREE_D_TYPE, urlTemplate)))
                                                          .flatMap(i -> i));
    }

    @Test
    void mapUnknownExtension() {
        final String url = String.format(URL_TEMPLATES.get(0), "foo");

        final Optional<String> result = mapper.map(url);

        assertThat(result).isNotPresent();
    }

    @Test
    void mapNoExtension() {
        final String url = "https://png.pngtree.com/element_our/20200610/ourmid";

        final Optional<String> result = mapper.map(url);

        assertThat(result).isNotPresent();
    }
}
