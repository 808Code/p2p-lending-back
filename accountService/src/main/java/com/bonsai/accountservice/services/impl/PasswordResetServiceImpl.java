package com.bonsai.accountservice.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bonsai.accountservice.dto.request.UserAuth;
import com.bonsai.accountservice.models.PasswordResetToken;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.PasswordResetTokenRepository;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.PasswordResetService;
import com.bonsai.accountservice.services.UserCredentialService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    //@Value("${account.app.url}")
    private String appUrl = "http://localhost:8081";

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EmailServiceImpl emailService;
    private final UserCredentialService userService;
    private final UserCredentialRepo userCredentialRepo;

    @Transactional
    public void createPasswordResetToken(String email) {

        UserCredential userCredential = userCredentialRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("Account of this email doesn't exist", HttpStatus.BAD_REQUEST));

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUserCredential(userCredential);
        passwordResetTokenRepository.save(passwordResetToken);

        // Send password reset email with token value
        String resetUrl = appUrl + "/reset-password?token=" + passwordResetToken.getToken();
        String subject = "Reset your password";
        String text = "To reset your password, click the link below:\n\n" + resetUrl;
        emailService.sendEmail(email, subject, text);
    }

    @Transactional
    public void resetPassword(String tokenValue, String newPassword) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new AppException("Invalid password reset token", HttpStatus.BAD_REQUEST));

        if (token.getExpiryDate().isAfter(LocalDateTime.now())) {
            UserCredential user = token.getUserCredential();
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userCredentialRepo.save(user);
            token.setUsed(true);
            token.setUsedDate(LocalDateTime.now());
            passwordResetTokenRepository.save(token);
//            passwordResetTokenRepository.delete(token);
        } else {
            throw new AppException("Password reset token has expired", HttpStatus.BAD_REQUEST);
        }
    }
}

