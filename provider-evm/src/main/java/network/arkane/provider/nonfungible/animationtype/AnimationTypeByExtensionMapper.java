package network.arkane.provider.nonfungible.animationtype;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public class AnimationTypeByExtensionMapper {

    public static final String IMAGE_TYPE = "image";
    public static final String VIDEO_TYPE = "video";
    public static final String AUDIO_TYPE = "audio";
    public static final String THREE_D_TYPE = "3d";
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("gif", "png", "jpg", "jpeg", "bmp", "webp", "svg");
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("mp4", "webm", "m4v", "ogv", "ogm", "ogg");
    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList("mp3", "mav", "oga");
    private static final List<String> THREE_D_EXTENSIONS = Arrays.asList("glb", "gltf");

    private static final ListMultimap<String, String> EXTENSIONS_MAP = ArrayListMultimap.create();

    static {
        EXTENSIONS_MAP.putAll(IMAGE_TYPE, IMAGE_EXTENSIONS);
        EXTENSIONS_MAP.putAll(VIDEO_TYPE, VIDEO_EXTENSIONS);
        EXTENSIONS_MAP.putAll(AUDIO_TYPE, AUDIO_EXTENSIONS);
        EXTENSIONS_MAP.putAll(THREE_D_TYPE, THREE_D_EXTENSIONS);
    }

    public Optional<String> map(final String url) {
        final String extension = extractExtension(url);
        return EXTENSIONS_MAP.keySet()
                             .stream()
                             .filter(key -> EXTENSIONS_MAP.get(key)
                                                          .contains(extension))
                             .findFirst();
    }

    private String extractExtension(final String url) {
        final String normalizedUrl = new StringTokenizer(url, "?")
                .nextToken();
        final int lastIndexOfPeriod = normalizedUrl.lastIndexOf('.');
        if (lastIndexOfPeriod > 0 && lastIndexOfPeriod < normalizedUrl.length() - 1) {
            return normalizedUrl.substring(lastIndexOfPeriod + 1);
        }
        return "";
    }
}
