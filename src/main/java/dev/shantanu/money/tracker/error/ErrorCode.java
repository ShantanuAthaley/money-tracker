package dev.shantanu.money.tracker.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    FILE_CREATION_ERROR("Could not create file", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    DB_OPERATION("Database operation error", HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final String errorDetails;
    private final int status;

    ErrorCode(String errorDetails, int status) {
        this.errorDetails = errorDetails;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}
