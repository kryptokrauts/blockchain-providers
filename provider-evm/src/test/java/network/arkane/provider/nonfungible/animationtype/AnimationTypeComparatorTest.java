package network.arkane.provider.nonfungible.animationtype;

import network.arkane.provider.nonfungible.domain.TypeValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.AUDIO_TYPE;
import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.IMAGE_TYPE;
import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.THREE_D_TYPE;
import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.VIDEO_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

class AnimationTypeComparatorTest {

    private AnimationTypeComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new AnimationTypeComparator();
    }

    @Test
    void compareVideo() {
        final TypeValue videoTV = TypeValue.builder().type(VIDEO_TYPE).build();
        final TypeValue threeDTV = TypeValue.builder().type(THREE_D_TYPE).build();
        final TypeValue audioTV = TypeValue.builder().type(AUDIO_TYPE).build();
        final TypeValue imageTV = TypeValue.builder().type(IMAGE_TYPE).build();
        final TypeValue unknownTV = TypeValue.builder().type("unknown").build();

        assertThat(comparator.compare(videoTV, videoTV)).isEqualTo(0);
        assertThat(comparator.compare(videoTV, threeDTV)).isEqualTo(-1);
        assertThat(comparator.compare(videoTV, audioTV)).isEqualTo(-1);
        assertThat(comparator.compare(videoTV, imageTV)).isEqualTo(-1);
        assertThat(comparator.compare(videoTV, unknownTV)).isEqualTo(-1);
    }

    @Test
    void compare3D() {
        final TypeValue videoTV = TypeValue.builder().type(VIDEO_TYPE).build();
        final TypeValue threeDTV = TypeValue.builder().type(THREE_D_TYPE).build();
        final TypeValue audioTV = TypeValue.builder().type(AUDIO_TYPE).build();
        final TypeValue imageTV = TypeValue.builder().type(IMAGE_TYPE).build();
        final TypeValue unknownTV = TypeValue.builder().type("unknown").build();

        assertThat(comparator.compare(threeDTV, videoTV)).isEqualTo(1);
        assertThat(comparator.compare(threeDTV, threeDTV)).isEqualTo(0);
        assertThat(comparator.compare(threeDTV, audioTV)).isEqualTo(-1);
        assertThat(comparator.compare(threeDTV, imageTV)).isEqualTo(-1);
        assertThat(comparator.compare(threeDTV, unknownTV)).isEqualTo(-1);
    }

    @Test
    void compareAudio() {
        final TypeValue videoTV = TypeValue.builder().type(VIDEO_TYPE).build();
        final TypeValue threeDTV = TypeValue.builder().type(THREE_D_TYPE).build();
        final TypeValue audioTV = TypeValue.builder().type(AUDIO_TYPE).build();
        final TypeValue imageTV = TypeValue.builder().type(IMAGE_TYPE).build();
        final TypeValue unknownTV = TypeValue.builder().type("unknown").build();

        assertThat(comparator.compare(audioTV, videoTV)).isEqualTo(1);
        assertThat(comparator.compare(audioTV, threeDTV)).isEqualTo(1);
        assertThat(comparator.compare(audioTV, audioTV)).isEqualTo(0);
        assertThat(comparator.compare(audioTV, imageTV)).isEqualTo(-1);
        assertThat(comparator.compare(audioTV, unknownTV)).isEqualTo(-1);
    }

    @Test
    void compareImage() {
        final TypeValue videoTV = TypeValue.builder().type(VIDEO_TYPE).build();
        final TypeValue threeDTV = TypeValue.builder().type(THREE_D_TYPE).build();
        final TypeValue audioTV = TypeValue.builder().type(AUDIO_TYPE).build();
        final TypeValue imageTV = TypeValue.builder().type(IMAGE_TYPE).build();
        final TypeValue unknownTV = TypeValue.builder().type("unknown").build();

        assertThat(comparator.compare(imageTV, videoTV)).isEqualTo(1);
        assertThat(comparator.compare(imageTV, threeDTV)).isEqualTo(1);
        assertThat(comparator.compare(imageTV, audioTV)).isEqualTo(1);
        assertThat(comparator.compare(imageTV, imageTV)).isEqualTo(0);
        assertThat(comparator.compare(imageTV, unknownTV)).isEqualTo(-1);
    }

    @Test
    void compareUnknown() {
        final TypeValue videoTV = TypeValue.builder().type(VIDEO_TYPE).build();
        final TypeValue threeDTV = TypeValue.builder().type(THREE_D_TYPE).build();
        final TypeValue audioTV = TypeValue.builder().type(AUDIO_TYPE).build();
        final TypeValue imageTV = TypeValue.builder().type(IMAGE_TYPE).build();
        final TypeValue unknownTV = TypeValue.builder().type("unknown").build();

        assertThat(comparator.compare(unknownTV, videoTV)).isEqualTo(1);
        assertThat(comparator.compare(unknownTV, threeDTV)).isEqualTo(1);
        assertThat(comparator.compare(unknownTV, audioTV)).isEqualTo(1);
        assertThat(comparator.compare(unknownTV, imageTV)).isEqualTo(1);
        assertThat(comparator.compare(unknownTV, unknownTV)).isEqualTo(0);
    }

    @Test
    void compareNulls() {
        final TypeValue someTV = TypeValue.builder().type("someType").build();
        final TypeValue nullTV = TypeValue.builder().type(null).build();

        assertThat(comparator.compare(null, null)).isEqualTo(0);
        assertThat(comparator.compare(someTV, null)).isEqualTo(-1);
        assertThat(comparator.compare(null, someTV)).isEqualTo(1);

        assertThat(comparator.compare(nullTV, nullTV)).isEqualTo(0);
        assertThat(comparator.compare(someTV, nullTV)).isEqualTo(-1);
        assertThat(comparator.compare(nullTV, someTV)).isEqualTo(1);
    }

    @Test
    void orderCollection() {
        final TypeValue videoTV = TypeValue.builder().type(VIDEO_TYPE).build();
        final TypeValue threeDTV = TypeValue.builder().type(THREE_D_TYPE).build();
        final TypeValue audioTV = TypeValue.builder().type(AUDIO_TYPE).build();
        final TypeValue imageTV = TypeValue.builder().type(IMAGE_TYPE).build();
        final TypeValue unknownTV = TypeValue.builder().type("unknown").build();

        final List<TypeValue> typeValues = Arrays.asList(audioTV, imageTV, videoTV, unknownTV, threeDTV);

        typeValues.sort(comparator);

        assertThat(typeValues).containsExactly(videoTV, threeDTV, audioTV, imageTV, unknownTV);
    }
}
