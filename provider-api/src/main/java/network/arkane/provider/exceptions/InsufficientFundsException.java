package network.arkane.provider.exceptions;

import lombok.Builder;

public class InsufficientFundsException extends UserInputException {

    @Builder(builderMethodName = "insufficientFundsException")
    public InsufficientFundsException(String errorCode,
                                      String message,
                                      Throwable cause) {
        super(errorCode, message, cause);
    }

}
