package com.bonsai.accountservice.dto.request;

public record CreateBorrowerRequest (
        String email,
        String password
){}
