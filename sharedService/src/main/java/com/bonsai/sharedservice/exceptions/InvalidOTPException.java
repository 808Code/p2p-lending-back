package com.bonsai.sharedservice.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidOTPException extends AppException {

    public InvalidOTPException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
