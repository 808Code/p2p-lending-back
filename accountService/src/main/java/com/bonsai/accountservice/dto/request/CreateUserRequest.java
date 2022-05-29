package com.bonsai.accountservice.dto.request;

public record CreateUserRequest(
        String email,
        String password
){}
