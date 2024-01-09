package com.master.bookstore_management.exceptions.exceptions;

import com.master.bookstore_management.exceptions.base.BaseException;
import org.springframework.http.HttpStatus;

public class DeletedBookException extends BaseException {
    public DeletedBookException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
