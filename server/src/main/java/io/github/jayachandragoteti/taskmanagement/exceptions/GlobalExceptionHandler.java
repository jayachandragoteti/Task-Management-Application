package io.github.jayachandragoteti.taskmanagement.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonNode;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final JsonNode errorMessages;

    public GlobalExceptionHandler(JsonNode errorMessages) {
        this.errorMessages = errorMessages;
    }

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<Map<String, Object>> handleCustomError(CustomErrorException ex) {
        String code = ex.getErrorCode();
        JsonNode errorCodesNode = errorMessages.get("error_codes");
        JsonNode customErrorList = errorMessages.get("custom_error_messages_list");
        String errorKey = null;
        if (errorCodesNode != null && errorCodesNode.has(code)) {
            errorKey = errorCodesNode.get(code).asText();
        }

        Map<String, Object> errorBody = new HashMap<>();
        int httpStatus = 500;
        if (errorKey != null && customErrorList != null && customErrorList.has(errorKey)) {
            JsonNode errorDetail = customErrorList.get(errorKey);
            if (ex.getCustomMessage() == null && ex.getDetails() == null) {
                httpStatus = errorDetail.get("status_code").asInt();
                errorBody.put("code", code);
                errorBody.put("message", errorDetail.get("message").asText());
                errorBody.put("details", errorDetail.get("details").asText());
            } else {
                errorBody.put("code", code);
                errorBody.put("message",
                        ex.getCustomMessage() != null ? ex.getCustomMessage() : errorDetail.get("message").asText());
                errorBody.put("details",
                        ex.getDetails() != null ? ex.getDetails() : errorDetail.get("details").asText());
                httpStatus = 500;
            }
        } else {
            // fallback to internal_server_error
            JsonNode internalError = customErrorList.get("internal_server_error");
            httpStatus = internalError.get("status_code").asInt();
            errorBody.put("code", "999");
            errorBody.put("message", internalError.get("message").asText());
            errorBody.put("details", internalError.get("details").asText());
        }

        Map<String, Object> finalErrorBody = new HashMap<>();
        finalErrorBody.put("error", errorBody);
        return ResponseEntity.status(httpStatus).body(finalErrorBody);
    }

    // Common handler for all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        JsonNode customErrorList = errorMessages.get("custom_error_messages_list");
        JsonNode internalError = customErrorList.get("internal_server_error");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("code", "999");
        errorBody.put("message", internalError.get("message").asText());
        // Use ex.getMessage() for details (you can customize this if needed)
        errorBody.put("details", ex.getMessage() != null ? ex.getMessage() : internalError.get("details").asText());

        Map<String, Object> finalErrorBody = new HashMap<>();
        finalErrorBody.put("error", errorBody);

        return ResponseEntity.status(internalError.get("status_code").asInt()).body(finalErrorBody);
    }
}
