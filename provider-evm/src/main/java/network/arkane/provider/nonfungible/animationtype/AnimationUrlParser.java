package network.arkane.provider.nonfungible.animationtype;

import network.arkane.provider.nonfungible.domain.TypeValue;

import java.util.Optional;

public class AnimationUrlParser {

    private static final String UNKNOWN_TYPE = "unknown";

    private final AnimationTypeByExtensionMapper byExtensionMapper;

    public AnimationUrlParser(final AnimationTypeByExtensionMapper byExtensionMapper) {
        this.byExtensionMapper = byExtensionMapper;
    }

    public TypeValue parse(final String url) {
        final Optional<String> typeOptional = byExtensionMapper.map(url);
        return TypeValue.builder()
                        .type(typeOptional.orElse(UNKNOWN_TYPE))
                        .value(url)
                        .build();
    }
}
