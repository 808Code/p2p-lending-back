package com.bonsai.accountservice.dto.request;

import com.bonsai.accountservice.models.UserCredential;

public record UserAuth(
        String email,
        String password,

        String role
) {
    public UserAuth(UserCredential userCredential) {
        this(userCredential.getEmail(), userCredential.getPassword(), userCredential.getRole());
    }
}
