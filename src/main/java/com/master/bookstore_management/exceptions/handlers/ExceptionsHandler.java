package com.master.bookstore_management.exceptions.handlers;

import com.master.bookstore_management.exceptions.exceptions.*;
import jakarta.validation.ConstraintViolationException;
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

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<Object> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex, WebRequest request) {
        return buildResponse(ex, request);
    }

    @ExceptionHandler(DeletedBookException.class)
    public ResponseEntity<Object> handleDeletedBookException(DeletedBookException ex, WebRequest request) {
        return buildResponse(ex, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {;
        return setResponseBody(ex, request, HttpStatus.NOT_FOUND, "Not Found");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return setResponseBody(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Validation failed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return setResponseBody(ex, request, HttpStatus.BAD_REQUEST, errors.toString());
    }

    private ResponseEntity<Object> setResponseBody(Exception ex,  WebRequest request, HttpStatus status, String error) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, status);
    }
}
