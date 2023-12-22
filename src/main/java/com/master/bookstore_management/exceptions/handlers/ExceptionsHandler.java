package com.master.bookstore_management.exceptions.handlers;

import com.master.bookstore_management.exceptions.exceptions.InvalidTokenException;
import com.master.bookstore_management.exceptions.exceptions.NoSuchElementInDatabaseException;
import com.master.bookstore_management.exceptions.exceptions.UnauthorizedUserException;
import com.master.bookstore_management.exceptions.exceptions.UserNotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionsHandler extends BaseExceptionHandler {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        return buildResponse(ex, request);
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<Object> handleUserNotLoggedInException(UserNotLoggedInException ex, WebRequest request) {
        return buildResponse(ex, request);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Object> handleUnauthorizedUserException(UnauthorizedUserException ex, WebRequest request) {
        return buildResponse(ex, request);
    }

    @ExceptionHandler(NoSuchElementInDatabaseException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementInDatabaseException ex, WebRequest request) {
        return buildResponse(ex, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage()); // You can customize the error message
        body.put("path", request.getDescription(false).replace("uri=", ""));


        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", errors);
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
