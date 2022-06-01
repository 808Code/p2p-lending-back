package com.bonsai.sharedservice.exceptions;

import org.springframework.http.HttpStatus;

public class EmailException extends AppException {

    public EmailException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
