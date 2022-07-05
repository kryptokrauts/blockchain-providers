package network.arkane.provider.exceptions;

public class UserInputException extends ArkaneException {

    public UserInputException(String errorCode,
                              String message,
                              Throwable cause) {
        super(message, cause, errorCode);
    }

    public UserInputException(String errorCode,
                              String message) {
        super(message, errorCode);
    }
}
