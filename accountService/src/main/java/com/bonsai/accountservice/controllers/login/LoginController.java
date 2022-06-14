package com.bonsai.accountservice.controllers.login;

import com.bonsai.accountservice.config.TokenHandler;
import com.bonsai.accountservice.dto.request.UserAuth;
import com.bonsai.accountservice.services.UserCredentialService;
import com.bonsai.sharedservice.dtos.response.LoginResponse;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/authenticate")
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserCredentialService userCredentialService;

    @PostMapping
    public ResponseEntity<SuccessResponse> authenticateUser(@RequestBody UserAuth user) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.email(), user.password())
            );
        } catch (AuthenticationException exception) {
            log.error("Authentication exception: {}", exception.getMessage());
            throw new AppException("Email or Password didn't match", HttpStatus.UNAUTHORIZED);
        }

        UserAuth foundUser = userCredentialService.findByEmail(user.email());
        return ResponseEntity.ok().body(
                new SuccessResponse("Login successful!",
                        new LoginResponse("Access token generated",
                                TokenHandler.generateToken(foundUser.email(), foundUser.role()),
                                foundUser.role()
                        )
                )
        );
    }
}
