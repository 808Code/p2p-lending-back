package com.bonsai.accountservice.services;

public interface PasswordResetService {
     void createPasswordResetToken(String email);
     void resetPassword(String tokenValue, String newPassword);
}
