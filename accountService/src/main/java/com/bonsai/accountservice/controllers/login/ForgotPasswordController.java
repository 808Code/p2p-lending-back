package com.bonsai.accountservice.controllers.login;

import com.bonsai.accountservice.dto.request.AdminKycMessageRequest;
import com.bonsai.accountservice.dto.request.ForgotPasswordRequest;
import com.bonsai.accountservice.dto.request.ResetPasswordRequest;
import com.bonsai.accountservice.services.KYCService;
import com.bonsai.accountservice.services.PasswordResetService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final KYCService kycService;
    private final PasswordResetService passwordResetService;


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.createPasswordResetToken(request.email());
        return ResponseEntity.ok(
                new SuccessResponse("Please See email for password reset link", null));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(
                new SuccessResponse("password reset successful", null));
    }
}
