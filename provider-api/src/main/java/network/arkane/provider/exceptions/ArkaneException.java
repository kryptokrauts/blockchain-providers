package network.arkane.provider.exceptions;

import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

public class ArkaneException extends RuntimeException {

    private static final String UNKNOWN_ERROR_CODE = "unknown-error";

    @Getter
    private final String errorCode;

    @Builder(builderMethodName = "arkaneException")
    public ArkaneException(final String message,
                           final Throwable cause,
                           final String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ArkaneException(String message,
                           String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ArkaneException(String message,
                           Throwable cause) {
        super(message, cause);
        this.errorCode = UNKNOWN_ERROR_CODE;
    }

    @JsonIgnore
    String getTrace() {
        try {
            return Tracing.currentTracer().currentSpan().context().traceIdString();
        } catch (final Exception ex) {
            return getAlternativeTraceId();
        }
    }

    @JsonIgnore
    private String getAlternativeTraceId() {
        try {
            if (CurrentTraceContext.Default.inheritable().get() != null) {
                return CurrentTraceContext.Default.inheritable().get().traceIdString();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }

    public String getTraceCode() {
        return getTrace();
    }

    public boolean hasErrorCode() {
        return errorCode != null;
    }

    @Override
    public String getMessage() {
        return this.getTraceCode() + " - " + super.getMessage();
    }
}
