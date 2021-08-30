package network.arkane.provider.exceptions;

public class ChainNoLongerSupportedException extends ArkaneException {

    public ChainNoLongerSupportedException() {
        super("This chain is no longer supported", "chain-no-longer-supported");
    }
}
