package io.github.jayachandragoteti.taskmanagement.models;

public class ErrorResponse {
    private String code;
    private String message;
    private String details;

    // Constructors
    public ErrorResponse(String code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
