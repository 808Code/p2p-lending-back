package com.bonsai.accountservice.dto.request;

import com.bonsai.accountservice.models.UserCredential;

public record UserCredentialDto(
        String email,
        String password,
        String role
) {
    public UserCredentialDto(UserCredential userCredential) {
        this(userCredential.getEmail(), userCredential.getPassword(),
                userCredential.getRole());
    }
}
