package network.arkane.provider.core.wallet;

public class CipherException extends Exception {

    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }
}
