package io.github.eyeve.auth;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.eyeve.dto.ApiError;
import io.github.eyeve.exception.DuplicateUsernameException;

@RestControllerAdvice
public class ApiExceptionHandler {

//    private static final Logger log = LogManager.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiError> handleDuplicateUsername(DuplicateUsernameException exception) {
        return build(HttpStatus.CONFLICT, "User already exists", exception.getMessage(), Map.of());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException exception) {
        /*
         * Do not reveal whether username or password was wrong. Detailed auth errors
         * help attackers enumerate accounts.
         */
        return build(HttpStatus.UNAUTHORIZED, "Authentication failed", "Invalid username or password", Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fields.put(error.getField(), error.getDefaultMessage())
        );

        return build(HttpStatus.BAD_REQUEST, "Validation failed", "Request body is invalid", fields);
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String error,
            String message,
            Map<String, String> fields
    ) {
        return ResponseEntity
                .status(status)
                .body(new ApiError(Instant.now(), status.value(), error, message, fields));
    }
}
