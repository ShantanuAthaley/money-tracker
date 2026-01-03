package dev.shantanu.money.tracker.error;

public class ApplicationException extends RuntimeException {
    private final ErrorCode code;
    public ApplicationException(String message, ErrorCode code) {
        this.code = code;
        super(message);
    }

    public ApplicationException(ErrorCode code, String message, Throwable cause) {
        this.code = code;
        super(message, cause);
    }

    public ErrorCode getCode() {
        return code;
    }
}
