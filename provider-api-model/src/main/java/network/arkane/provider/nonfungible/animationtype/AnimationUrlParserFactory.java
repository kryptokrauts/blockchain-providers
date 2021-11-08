package network.arkane.provider.nonfungible.animationtype;

public class AnimationUrlParserFactory {

    private AnimationUrlParserFactory() {
    }

    public static AnimationUrlParser create() {
        return new AnimationUrlParser(new AnimationTypeByExtensionMapper());
    }
}
