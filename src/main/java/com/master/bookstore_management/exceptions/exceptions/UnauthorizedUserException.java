package com.master.bookstore_management.exceptions.exceptions;

import com.master.bookstore_management.exceptions.base.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedUserException extends BaseException {
    public UnauthorizedUserException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

