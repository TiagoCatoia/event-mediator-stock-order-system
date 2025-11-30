package br.ifsp.stock_order.common.api;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Bad Request");
        errors.put("message", "Validation failed for one or more fields");

        Map<String, String> fieldErrorDetails = new HashMap<>();
        for (FieldError error : fieldErrors) {
            fieldErrorDetails.put(error.getField(), error.getDefaultMessage());
        }

        errors.put("details", fieldErrorDetails);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.NOT_FOUND.value());
        errors.put("error", "Not Found");
        errors.put("message", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleEntityExists(EntityExistsException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.CONFLICT.value());
        errors.put("error", "Conflict");
        errors.put("message", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }
}
