package com.bonsai.accountservice.dto.response;

public record SuccessResponse(
        String message,
        Object data
){}
