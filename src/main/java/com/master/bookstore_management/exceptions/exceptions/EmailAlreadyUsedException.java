package com.master.bookstore_management.exceptions.exceptions;

import com.master.bookstore_management.exceptions.base.BaseException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyUsedException extends BaseException  {
    public EmailAlreadyUsedException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
