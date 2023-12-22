package com.master.bookstore_management.exceptions.exceptions;

import com.master.bookstore_management.exceptions.base.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotLoggedInException extends BaseException {
    public UserNotLoggedInException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
