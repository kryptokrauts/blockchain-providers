package network.arkane.provider.exceptions;

public class BlockchainProviderResourceNotFoundException extends ArkaneException {
    public BlockchainProviderResourceNotFoundException(String message,
                                                       String errorCode) {
        super(message, errorCode);
    }
}
