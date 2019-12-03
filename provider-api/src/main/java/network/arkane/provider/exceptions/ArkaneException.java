package network.arkane.provider.exceptions;

import brave.Tracing;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    String getTrace() {
        try {
            return Tracing.currentTracer().currentSpan().context().traceIdString();
        } catch (final Exception ex) {
            //no tracing enabled;
            ex.printStackTrace();
            return null;
        }
    }

    public String getTraceCode() {
        return getTrace();
    }

    public ArkaneException(String message,
                           String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ArkaneException(String message,
                           Throwable cause) {
        super(message, cause);
    }

    public ArkaneException(final String errorCode,
                           final String message,
                           final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != null;
    }
}
