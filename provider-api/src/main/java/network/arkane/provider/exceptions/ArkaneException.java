package network.arkane.provider.exceptions;

import lombok.Builder;
import lombok.Getter;

public class ArkaneException extends RuntimeException {

    @Getter
    private String errorCode;

    @Builder(builderMethodName = "arkaneException")
    public ArkaneException(final String message,
                           final Throwable cause,
                           final String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ArkaneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ArkaneException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ArkaneException(final String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != null;
    }
}
