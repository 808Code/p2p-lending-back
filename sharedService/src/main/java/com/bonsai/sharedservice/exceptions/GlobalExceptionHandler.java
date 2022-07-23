package com.bonsai.sharedservice.exceptions;

import com.bonsai.sharedservice.dtos.response.DtoInvalidErrorResponse;
import com.bonsai.sharedservice.dtos.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAppException(AppException e){
        var errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DtoInvalidErrorResponse> handleMethodArgsNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(fieldName, message);
                }
        );

        DtoInvalidErrorResponse errorResponse = new DtoInvalidErrorResponse("Invalid Request", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
