package io.github.jayachandragoteti.taskmanagement.exceptions;

public class CustomErrorException extends RuntimeException {
    private final String errorCode;
    private final String message;
    private final String details;

    public CustomErrorException(String errorCode) {
        this(errorCode, null, null);
    }

    public CustomErrorException(String errorCode, String message, String details) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }

    public String getErrorCode() { return errorCode; }
    public String getCustomMessage() { return message; }
    public String getDetails() { return details; }
}
