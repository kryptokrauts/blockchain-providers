package network.arkane.provider.nonfungible.animationtype;

import network.arkane.provider.nonfungible.domain.TypeValue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.AUDIO_TYPE;
import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.IMAGE_TYPE;
import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.THREE_D_TYPE;
import static network.arkane.provider.nonfungible.animationtype.AnimationTypeByExtensionMapper.VIDEO_TYPE;

public class AnimationTypeComparator implements Comparator<TypeValue> {

    // Highest last
    private final static List<String> TYPES_ORDER = Arrays.asList(IMAGE_TYPE, AUDIO_TYPE, THREE_D_TYPE, VIDEO_TYPE);

    @Override
    public int compare(final TypeValue tv1,
                       final TypeValue tv2) {
        if (tv1 == null && tv2 == null) return 0;
        if (tv1 == null) return -1;
        if (tv2 == null) return 1;

        if (tv1.getType() == null && tv2.getType() == null) return 0;
        if (tv1.getType() == null) return -1;
        if (tv2.getType() == null) return 1;

        return Integer.compare(TYPES_ORDER.indexOf(tv1.getType()), TYPES_ORDER.indexOf(tv2.getType()));
    }
}
